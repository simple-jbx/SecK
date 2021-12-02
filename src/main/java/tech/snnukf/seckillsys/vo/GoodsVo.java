package tech.snnukf.seckillsys.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.snnukf.seckillsys.pojo.Goods;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author simple.jbx
 * @ClassName GoodsVo
 * @description 商品返回对象
 * @email jb.xue@qq.com
 * @github https://github.com/simple-jbx
 * @date 2021年10月11日 12:20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsVo extends Goods {
    private Integer stockCount;

    private BigDecimal seckillPrice;

    private Date startDate;

    private Date endDate;
}
