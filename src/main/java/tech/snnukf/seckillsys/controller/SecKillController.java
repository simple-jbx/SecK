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
     * @description //没有验证码的测试秒杀接口
     * @date 11:08 2021/12/3
     * @param	user
     * @param	goodsId
     * @return tech.snnukf.seckillsys.vo.RespBean
     **/
    @RequestMapping(value = "/doSeckill", method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSeckill(User user, Long goodsId) {
        if(user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }

        ValueOperations valueOperations = redisTemplate.opsForValue();

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

        Long stock = (Long) redisTemplate.execute(redisScript, Collections.singletonList("seckillGoods:" + goodsId),
                Collections.EMPTY_LIST);

        if (stock < 0) {
            EmptyStockMap.put(goodsId, true);
            //increment 不是原子操作不能这样  要么不改变 要么设为0
            //valueOperations.increment("seckillGoods:" + goodsId);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
        mqSender.sendSeckillMessage(JsonUtil.object2JsonStr(seckillMessage));
        return RespBean.success(0);
    }

    /**
     * @author simple.jbx
     * @description 带有地址校验和验证码校验的真实秒杀接口
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

        //ValueOperations valueOperations = redisTemplate.opsForValue();

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

        /**
        //Redis加锁预减库存 需要把握加锁与释放锁的时机
        Boolean isLock = valueOperations.setIfAbsent("lock", user.getId(),
                50, TimeUnit.MILLISECONDS);

        if(isLock) {
           //业务代码
        } else {
            return RespBean.error(RespBeanEnum.SECKILL_ERROR);
        }
         **/

        Long stock = (Long) redisTemplate.execute(redisScript, Collections.singletonList("seckillGoods:" + goodsId),
                Collections.EMPTY_LIST);
        if (stock < 0L) {
            EmptyStockMap.put(goodsId, true);
            //valueOperations.increment("seckillGoods:" + goodsId);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
        mqSender.sendSeckillMessage(JsonUtil.object2JsonStr(seckillMessage));
        return RespBean.success(0);
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

        Boolean check = orderService.checkCaptcha(user, goodsId, captcha);
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
