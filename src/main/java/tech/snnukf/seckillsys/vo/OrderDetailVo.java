package tech.snnukf.seckillsys.vo;

import tech.snnukf.seckillsys.pojo.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.snnukf.seckillsys.pojo.Order;

/**
 * @author simple.jbx
 * @description 订单详情返回对象
 * @date 20:00 2021/11/1
 * @return
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailVo {
	private Order order;

	private GoodsVo goodsVo;
}
