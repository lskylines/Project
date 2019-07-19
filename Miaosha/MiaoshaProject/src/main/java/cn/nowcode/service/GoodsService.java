package cn.nowcode.service;

import cn.nowcode.dao.GoodsDao;
import cn.nowcode.domain.MiaoshaGoods;
import cn.nowcode.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author
 * @create 2019-07-14 15:07
 */
@Service
public class GoodsService {
    @Autowired
    private GoodsDao goodsDao;
    public List<GoodsVo> listGoodsVo(){
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodsVoById(long goodsId){
        return goodsDao.getGoodsVoById(goodsId);
    }

    public boolean reduceStockCount(MiaoshaGoods miaoshaGoods){
        int ret = goodsDao.reduceStockCount(miaoshaGoods);
        return ret > 0;
    }
}
