package cn.nowcode.dao;

import cn.nowcode.domain.MiaoshaGoods;
import cn.nowcode.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author
 * @create 2019-07-14 14:58
 */
@Mapper
public interface GoodsDao {
    @Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id = g.id")
    public List<GoodsVo> listGoodsVo();

    @Select({"SELECT g.*, mg.miaosha_price, mg.stock_count, mg.start_date, mg.end_date FROM miaosha_goods mg LEFT JOIN goods g on mg.goods_id=g.id where g.id=#{goodsId}"})
    public GoodsVo getGoodsVoById(@Param("goodsId") long goodsId);

    @Update("UPDATE miaosha_goods SET stock_count=stock_count-1 WHERE goods_id=#{goodsId} and stock_count > 0")
    public int reduceStockCount(MiaoshaGoods miaoshaGoods);
}
