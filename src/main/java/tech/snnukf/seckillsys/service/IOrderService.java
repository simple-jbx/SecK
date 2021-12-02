package tech.snnukf.seckillsys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.snnukf.seckillsys.pojo.Order;
import tech.snnukf.seckillsys.pojo.User;
import tech.snnukf.seckillsys.vo.GoodsVo;
import tech.snnukf.seckillsys.vo.OrderDetailVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author simple.jbx
 */
public interface IOrderService extends IService<Order> {

    /**
     * @author simple.jbx
     * @description 秒杀
     * @date 20:02 2021/11/1
     * @param	user
     * @param	goods
     * @return tech.snnukf.seckillsys.pojo.Order
     **/
    Order seckill(User user, GoodsVo goods);

    /**
     * @author simple.jbx
     * @description 订单详情
     * @date 20:44 2021/10/30
     * @param	orderId
     * @return tech.snnukf.seckillsys.vo.OrderDetailVo
     **/
    OrderDetailVo detail(Long orderId);


    /**
     * @author simple.jbx
     * @description 获取秒杀地址
     * @date 19:51 2021/11/22
     * @param	user
     * @param	goodsId
     * @return java.lang.String
     **/
    String createPath(User user, Long goodsId);

    /**
     * @author simple.jbx
     * @description 校验秒杀地址
     * @date 19:59 2021/11/22
     * @param    user
     * @param    goodsId
     * @param path
     * @return boolean
     **/
    boolean checkPath(User user, Long goodsId, String path);

    /**
     * @author simple.jbx
     * @description 校验验证码
     * @date 20:41 2021/11/22
     * @param	user
     * @param	goodsId
     * @param	captcha
     * @return java.lang.Boolean
     **/
    Boolean chechCaptcha(User user, Long goodsId, String captcha);
}
