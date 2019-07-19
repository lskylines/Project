package cn.nowcode.rabbitmq;

import cn.nowcode.domain.MiaoshaOrder;
import cn.nowcode.domain.MiaoshaUser;
import cn.nowcode.redis.RedisService;
import cn.nowcode.service.GoodsService;
import cn.nowcode.service.MiaoshaService;
import cn.nowcode.service.OrderService;
import cn.nowcode.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author
 * @create 2019-07-15 15:57
 */
@Service
public class MQReceiver {
    private static final Logger logger = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MiaoshaService miaoshaService;
    @Autowired
    private RedisService redisService;
   /* @RabbitListener(queues = MQConfig.queue)
    public void receiver(String message){
        logger.info("receiver message:" + message);

    }*/

   @RabbitListener(queues = MQConfig.miaosha_queue)
    public void receiver(String msg){
       MiaoshaMessage mm = RedisService.StringToBean(msg, MiaoshaMessage.class);
       MiaoshaUser user = mm.getUser();
       long goodsId  = mm.getGoodsId();
       GoodsVo goods  =     goodsService.getGoodsVoById(goodsId);
       int stock =goods.getStockCount();
       if(stock <= 0){
           return ;
       }
      //判断是否已经秒杀
       MiaoshaOrder order =  orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
       if(order!=null){
           return ;
       }
       miaoshaService.miaosha(user,goods);
   }

}
