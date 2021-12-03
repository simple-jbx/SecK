package tech.snnukf.seckillsys.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tech.snnukf.seckillsys.pojo.SeckillMessage;
import tech.snnukf.seckillsys.pojo.SeckillOrder;
import tech.snnukf.seckillsys.pojo.User;
import tech.snnukf.seckillsys.service.IGoodsService;
import tech.snnukf.seckillsys.service.IOrderService;
import tech.snnukf.seckillsys.utils.JsonUtil;
import tech.snnukf.seckillsys.vo.GoodsVo;
import tech.snnukf.seckillsys.vo.RespBean;
import tech.snnukf.seckillsys.vo.RespBeanEnum;

import javax.jws.soap.SOAPBinding;

/**
 * @author simple.jbx
 * @ClassName MQRecevier
 * @description //TODO
 * @email jb.xue@qq.com
 * @github https://github.com/simple-jbx
 * @date 2021/11/03/ 20:39
 */
@Service
@Slf4j
public class MQRecevier {
//    @RabbitListener(queues = "queue")
//    public void recive(Object msg) {
//        log.info("接收消息：" + msg);
//    }
//
//    @RabbitListener(queues = "queue_fanout01")
//    public void recive01(Object msg) {
//        log.info("QUEUE01接收消息：" + msg);
//    }
//
//    @RabbitListener(queues = "queue_fanout02")
//    public void receive02(Object msg) {
//        log.info("QUEUE02接收消息：" + msg);
//    }
//
//    @RabbitListener(queues = "queue_direct01")
//    public void receive03(Object msg) {
//        log.info("QUEUE01接收消息：" + msg);
//    }
//
//    @RabbitListener(queues = "queue_direct02")
//    public void receive04(Object msg) {
//        log.info("QUEUE02接收消息：" + msg);
//    }
//
//    @RabbitListener(queues = "queue_topic01")
//    public void receive05(Object msg) {
//        log.info("QUEUE01接收消息：" + msg);
//    }
//
//    @RabbitListener(queues = "queue_topic02")
//    private void receive06(Object msg) {
//        log.info("QUEUE02接收消息：" + msg);
//    }
//
//    @RabbitListener(queues = "queue_header01")
//    private void receive07(Message msg) {
//        log.info("QUEUE01接收Message对象：" + msg);
//        log.info("QUEUE01接收消息：" + new String(msg.getBody()));
//    }
//
//    @RabbitListener(queues = "queue_header02")
//    private void receive08(Message msg) {
//        log.info("QUEUE02接收Message对象：" + msg);
//        log.info("QUEUE02接收消息：" + new String(msg.getBody()));
//    }

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IOrderService orderService;

    /**
     * @author simple.jbx
     * @description 下单操作
     * @date 19:52 2021/11/8
     * @param	message
     * @return void
     **/
    @RabbitListener(queues = "seckillQueue")
    public void receiveMessage(String message) {
        //log.info("接收的消息：" + message);
        SeckillMessage seckillMessage = JsonUtil.jsonStr2Object(message, SeckillMessage.class);
        Long goodId = seckillMessage.getGoodId();
        User user = seckillMessage.getUser();

        //判断库存
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodId);
        if(goodsVo.getStockCount() < 1) {
            return;
        }

        //判断是否重复抢购
        SeckillOrder seckillOrder = (SeckillOrder)redisTemplate.opsForValue()
                .get("order:" + user.getId() + ":" + goodId);

        if(seckillOrder != null) {
            return;
        }

        //下单操作
        orderService.seckill(user, goodsVo);
    }

}
