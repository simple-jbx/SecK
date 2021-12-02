package tech.snnukf.seckillsys.controller;

import com.wf.captcha.ArithmeticCaptcha;
//不用管这个报错
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.snnukf.seckillsys.config.AccessLimit;
import tech.snnukf.seckillsys.exception.GlobalException;
import tech.snnukf.seckillsys.pojo.SeckillMessage;
import tech.snnukf.seckillsys.pojo.SeckillOrder;
import tech.snnukf.seckillsys.pojo.User;
import tech.snnukf.seckillsys.rabbitmq.MQSender;
import tech.snnukf.seckillsys.service.IGoodsService;
import tech.snnukf.seckillsys.service.IOrderService;
import tech.snnukf.seckillsys.service.ISeckillOrderService;
import tech.snnukf.seckillsys.utils.JsonUtil;
import tech.snnukf.seckillsys.vo.GoodsVo;
import tech.snnukf.seckillsys.vo.RespBean;
import tech.snnukf.seckillsys.vo.RespBeanEnum;

/**
 * @author simple.jbx
 * @ClassName SecKillController
 * @description 秒杀Controller
 * Windows 优化前 QPS 800
 *         页面缓存 QPS 1400
 *         优化QPS（进一步减少数据库操作 redis判断是否重复抢购 是否没库存了） 2500
 * @email jb.xue@qq.com
 * @github https://github.com/simple-jbx
 * @date 2021/10/17/ 09:42
 */
@Slf4j
@Controller
@RequestMapping("/secKill")
public class SecKillController implements InitializingBean {

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private ISeckillOrderService seckillOrderService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MQSender mqSender;

    @Autowired
    private RedisScript<Long> redisScript;

    private Map<Long, Boolean> EmptyStockMap = new HashMap<>();

    /**
     * @author simple.jbx
     * @description 秒杀
     * @date 09:43 2021/10/17
     * @param	user
     * @param	goodsId
     * @return java.lang.String
     **/
    @RequestMapping(value = "/{path}/doSeckill", method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSeckill(@PathVariable String path, User user, Long goodsId) {
        if(user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }

        ValueOperations valueOperations = redisTemplate.opsForValue();

        boolean check = orderService.checkPath(user, goodsId, path);
        if(!check) {
            return RespBean.error(RespBeanEnum.REQUEST_ILLEGAL);
        }

        //判断是否重复抢购
        SeckillOrder seckillOrder = (SeckillOrder)redisTemplate.opsForValue()
                .get("order:" + user.getId() + ":" + goodsId);

        if(seckillOrder != null) {
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }

        //内存标记，减少Redis访问
        if(EmptyStockMap.get(goodsId)) {
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        //预减库存
        Long stock = valueOperations.decrement("seckillGoods:" + goodsId);

        if(stock < 0) {
            valueOperations.increment("seckillGoods:" + goodsId);
            EmptyStockMap.put(goodsId, true);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
        mqSender.sendSeckillMessage(JsonUtil.object2JsonStr(seckillMessage));

        return RespBean.success(0);

//        //Redis分布式锁预减库存 释放锁得时机不好把握  不如上面那个方法
//        Boolean isLock = valueOperations.setIfAbsent("lock", user.getId(),
//                50, TimeUnit.MILLISECONDS);
//
//        if(isLock) {
//            List<String> scriptKeysList = new ArrayList<>(2);
//            scriptKeysList.add("lock");
//            scriptKeysList.add("seckillGoods:" + goodsId);
//            Long stock = (Long) redisTemplate.execute(redisScript, scriptKeysList, user.getId());
//            if(stock < 0) {
//                valueOperations.increment("seckillGoods:" + goodsId);
//                EmptyStockMap.put(goodsId, true);
//                return RespBean.error(RespBeanEnum.EMPTY_STOCK);
//            }
//
//            SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
//            mqSender.sendSeckillMessage(JsonUtil.object2JsonStr(seckillMessage));
//
//            return RespBean.success(0);
//        } else {
//            return RespBean.error(RespBeanEnum.SECKILL_ERROR);
//        }


//        Order order = orderService.seckill(user, goods);


//        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
//
//        //判断库存
//        if(goods.getStockCount() < 1) {
//            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
//        }


//        //判断是否重复抢购
//        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id",
//                user.getId()).eq("goods_id", goodsId));


//        Order order = orderService.seckill(user, goods);
//        System.out.println("seckill success");
//        return RespBean.success(order);
    }

    /**
     * @author simple.jbx
     * @description 系统初始化，把商品库存数量加载到Redis
     * @date 19:24 2021/11/8
     * @param
     * @return void
     **/
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list = goodsService.findGoodsVo();
        if(CollectionUtils.isEmpty(list)) {
            return;
        }

        list.forEach(goodsVo -> {
                    redisTemplate.opsForValue().set("seckillGoods:" + goodsVo.getId(),
                            goodsVo.getStockCount());
                    EmptyStockMap.put(goodsVo.getId(), false);
                });
    }


    /**
     * @author simple.jbx
     * @description 获取秒杀结果
     * @date 19:50 2021/11/9
     * @param	goodsId
     * @return tech.snnukf.seckillsys.vo.RespBean
     **/
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public RespBean getResult(User user, Long goodsId) {
        if(user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }

        Long orderId = seckillOrderService.getResult(user, goodsId);
        return RespBean.success(orderId);
    }

    @AccessLimit(second=5, maxCount=5, needLogin=true)
    @RequestMapping(value = "/path", method = RequestMethod.GET)
    @ResponseBody
    public RespBean getPath(User user, Long goodsId, String captcha) {
        if(user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }

        Boolean check = orderService.chechCaptcha(user, goodsId, captcha);
        if(!check) {
            return RespBean.error(RespBeanEnum.ERROR_CAPTCHA);
        }

        String str = orderService.createPath(user, goodsId);
        return RespBean.success(str);
    }

    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public void  verifyCode(User user, Long goodsId, HttpServletResponse response) {
        if(user == null || goodsId < 0) {
            throw new GlobalException(RespBeanEnum.REQUEST_ILLEGAL);
        }

        //设置请求头为输出图片类型
        response.setContentType("image/jpg");
        response.setHeader("Pargam", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        //生成验证码，将结果放入Redis
        ArithmeticCaptcha captcha = new ArithmeticCaptcha();
        redisTemplate.opsForValue().set("captcha:" + user.getId() + ":" + goodsId,
                captcha.text(), 300, TimeUnit.SECONDS);
        try {
            captcha.out(response.getOutputStream());
        } catch (IOException e) {
           log.error("验证码生成失败", e.getMessage());
        }
    }
}
