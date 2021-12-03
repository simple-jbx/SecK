package tech.snnukf.seckillsys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.snnukf.seckillsys.exception.GlobalException;
import tech.snnukf.seckillsys.mapper.OrderMapper;
import tech.snnukf.seckillsys.pojo.Order;
import tech.snnukf.seckillsys.pojo.SeckillGoods;
import tech.snnukf.seckillsys.pojo.SeckillOrder;
import tech.snnukf.seckillsys.pojo.User;
import tech.snnukf.seckillsys.service.IGoodsService;
import tech.snnukf.seckillsys.service.IOrderService;
import tech.snnukf.seckillsys.service.ISeckillGoodsService;
import tech.snnukf.seckillsys.service.ISeckillOrderService;
import tech.snnukf.seckillsys.utils.MD5Util;
import tech.snnukf.seckillsys.utils.UUIDUtil;
import tech.snnukf.seckillsys.vo.GoodsVo;
import tech.snnukf.seckillsys.vo.OrderDetailVo;
import tech.snnukf.seckillsys.vo.RespBeanEnum;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author simple.jbx
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    @Autowired
    private ISeckillOrderService seckillOrderService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ISeckillGoodsService seckillGoodsService;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @author simple.jbx
     * @description 秒杀
     * @date 09:58 2021/10/17
     * @param	user
     * @param	goods
     * @return tech.snnukf.seckillsys.pojo.Order
     **/
    @Transactional
    @Override
    public Order seckill(User user, GoodsVo goods) {

        //ValueOperations valueOperations = redisTemplate.opsForValue();

        //秒杀商品减库存
        SeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>()
            .eq("goods_id", goods.getId()));
        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
        boolean seckillGoodsResult = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>()
                .setSql("stock_count = stock_count - 1").eq("goods_id", goods.getId())
                .gt("stock_count", 0));

        //前面Controller层有内存标记 这个会不起作用
        //if(seckillGoods.getStockCount() < 1) {
            //valueOperations.set("isStockEmpty:" + goods.getId(), 0);
            //return null;
        //}

        //生成订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);

        //秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodsId(goods.getId());
        seckillOrderService.save(seckillOrder);
        redisTemplate.opsForValue().set("order:"+user.getId()+":"+goods.getId(), seckillOrder);
        return order;
    }

    /**
     * @author simple.jbx
     * @description 订单详情
     * @date 20:46 2021/10/30
     * @param	orderId
     * @return tech.snnukf.seckillsys.vo.OrderDetailVo
     **/
    @Override
    public OrderDetailVo detail(Long orderId) {
        if (orderId == null) {
            throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
        }
        Order order = orderMapper.selectById(orderId);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(order.getGoodsId());
        OrderDetailVo detail = new OrderDetailVo();
        detail.setOrder(order);
        detail.setGoodsVo(goodsVo);
        return detail;
    }

    /**
     * @author simple.jbx
     * @description 获取秒杀地址
     * @date 19:52 2021/11/22
     * @param	user
     * @param	goodsId
     * @return java.lang.String
     **/
    @Override
    public String createPath(User user, Long goodsId) {
        String str = MD5Util.md5(UUIDUtil.uuid() + "seckill");
        redisTemplate.opsForValue().set("seckillPath:" + user.getId() + ":" + goodsId,str,
            60, TimeUnit.SECONDS);
        return str;
    }

    /**
     * @author simple.jbx
     * @description 校验秒杀地址
     * @date 20:00 2021/11/22
     * @param	user
     * @param	goodsId
     * @param	path
     * @return boolean
     **/
    @Override
    public boolean checkPath(User user, Long goodsId, String path) {
        if(user == null || goodsId < 0 || StringUtils.isEmpty(path)) {
            return false;
        }

        String redisPath = (String) redisTemplate.opsForValue().get("seckillPath:" + user.getId() +
                ":" + goodsId);
        return path.equals(redisPath);
    }

    @Override
    public Boolean checkCaptcha(User user, Long goodsId, String captcha) {
        if(user == null || goodsId < 0 || StringUtils.isEmpty(captcha)) {
            return false;
        }

        String redisCaptcha = (String) redisTemplate.opsForValue().get("captcha:" +
                user.getId() + ":" + goodsId);
        return captcha.equals(redisCaptcha);
    }
}
