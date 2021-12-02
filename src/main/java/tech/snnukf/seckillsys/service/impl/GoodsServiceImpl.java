package tech.snnukf.seckillsys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.snnukf.seckillsys.mapper.GoodsMapper;
import tech.snnukf.seckillsys.pojo.Goods;
import tech.snnukf.seckillsys.service.IGoodsService;
import tech.snnukf.seckillsys.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author simple.jbx
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Autowired
    private GoodsMapper goodMapper;

    @Override
    public List<GoodsVo> findGoodsVo() {
        return goodMapper.findGoodsVo();
    }

    /**
     * @author simple.jbx
     * @description 获取商品详情
     * @date 15:12 2021/10/12
     * @param	goodsId
     * @return java.lang.String
     **/
    @Override
    public GoodsVo findGoodsVoByGoodsId(Long goodsId) {
        return goodMapper.findGoodsVoByGoodsId(goodsId);
    }

}
