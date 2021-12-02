package tech.snnukf.seckillsys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import tech.snnukf.seckillsys.pojo.Goods;
import tech.snnukf.seckillsys.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author simple.jbx
 */
public interface IGoodsService extends IService<Goods> {

    /**
     * @author simple.jbx
     * @description 获取商品列表
     * @date 12:24 2021/10/11
     * @param	
     * @return java.util.List<tech.snnukf.seckillsys.vo.GoodsVo>
     **/
    List<GoodsVo> findGoodsVo();

    /**
     * @author simple.jbx
     * @description 获取商品详情
     * @date 15:14 2021/10/12
     * @param	goodsId
     * @return java.lang.String
     **/
    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
