package tech.snnukf.seckillsys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.snnukf.seckillsys.pojo.SeckillOrder;
import tech.snnukf.seckillsys.pojo.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author simple.jbx
 */
public interface ISeckillOrderService extends IService<SeckillOrder> {

    Long getResult(User user, Long goodsId);
}
