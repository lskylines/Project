package cn.nowcode.service;

import cn.nowcode.dao.MiaoshaOrderDao;
import cn.nowcode.domain.Goods;
import cn.nowcode.domain.MiaoshaOrder;
import cn.nowcode.domain.MiaoshaUser;
import cn.nowcode.domain.OrderInfo;
import cn.nowcode.redis.OrderKey;
import cn.nowcode.redis.RedisService;
import cn.nowcode.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author
 * @create 2019-07-14 16:21
 */
@Service
public class OrderService {
    @Autowired
    private MiaoshaOrderDao miaoshaOrderDao;
    @Autowired
    private RedisService redisService;
    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId){
        //return miaoshaOrderDao.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
        return redisService.get(OrderKey.getMiaoshaOrderByUidGid, ""+userId+"_"+goodsId,MiaoshaOrder.class);
    }

    @Transactional
    public OrderInfo createOrder(MiaoshaUser user, GoodsVo goodsVo){
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setGoodsId(goodsVo.getId());
        orderInfo.setUserId(user.getId());
        orderInfo.setStatus(0);
        orderInfo.setOrderChannel(1);
        orderInfo.setGoodsPrice(goodsVo.getMiaoshaPrice());
        orderInfo.setGoodsName(goodsVo.getGoodsName());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setPayDate(new Date());
        orderInfo.setGoodsCount(1);
        miaoshaOrderDao.InsertOrderInfo(orderInfo);
        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setUserId(user.getId());
        miaoshaOrder.setOrderId(orderInfo.getId());
        miaoshaOrder.setGoodsId(goodsVo.getId());
        miaoshaOrderDao.inserMiaoshaOrder(miaoshaOrder);
        redisService.set(OrderKey.getMiaoshaOrderByUidGid, ""+user.getId()+"_"+goodsVo.getId(), miaoshaOrder);
        return orderInfo;
    }

    public OrderInfo getByOrderId(long orderId){
        return miaoshaOrderDao.getByOrderId(orderId);
    }
}
