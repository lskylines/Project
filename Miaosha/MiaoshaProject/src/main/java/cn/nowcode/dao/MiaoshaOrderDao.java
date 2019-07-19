package cn.nowcode.dao;

import cn.nowcode.domain.MiaoshaOrder;
import cn.nowcode.domain.OrderInfo;
import org.apache.ibatis.annotations.*;

/**
 * @author
 * @create 2019-07-14 16:22
 */
@Mapper
public interface MiaoshaOrderDao {
    @Select("SELECT * FROM miaosha_order WHERE user_id=#{userId} AND goods_id=#{goodsId}")
    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(@Param("userId")long userId, @Param("goodsId")long goodsId);

    @Insert("INSERT INTO order_info(user_id,goods_id,delivery_addr_id, goods_name, goods_count, goods_price,order_channel, status,create_date,pay_date) VALUES(" +
            "#{userId}, #{goodsId}, #{deliveryAddrId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel}, #{status}, #{createDate}, #{payDate})")
    @SelectKey(keyProperty = "id", keyColumn = "id", resultType = long.class, before = false, statement = "select last_insert_id()")
    public int InsertOrderInfo(OrderInfo orderInfo);

    @Insert("INSERT INTO miaosha_order(user_id, order_id, goods_id) VALUES(" +
            "#{userId}, #{orderId}, #{goodsId})")
    public int inserMiaoshaOrder(MiaoshaOrder miaoshaOrder);

    @Select("SELECT * FROM order_info WHERE id=#{orderId}")
    public OrderInfo getByOrderId(@Param("orderId")long orderId);
}
