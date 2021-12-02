package tech.snnukf.seckillsys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.snnukf.seckillsys.pojo.Goods;
import tech.snnukf.seckillsys.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author simple.jbx
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    /**
     * @author simple.jbx
     * @description 获取商品列表
     * @date 15:20 2021/10/12
     * @param	
     * @return java.util.List<tech.snnukf.seckillsys.vo.GoodsVo>
     **/
    List<GoodsVo> findGoodsVo();

    /**
     * @author simple.jbx
     * @description 获取商品详情
     * @date 15:14 2021/10/12
     * @param
     * @return java.lang.String
     **/
    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
