package cn.nowcode.rabbitmq;

import cn.nowcode.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author
 * @create 2019-07-15 15:48
 */
@Service
public class MQSender {
    private static final Logger logger = LoggerFactory.getLogger(MQSender.class);

    @Autowired
    private AmqpTemplate amqpTemplate;
   /* public void send(Object message){
        String msg = RedisService.BeanToString(message);
        logger.info("send message:" + message);
        amqpTemplate.convertAndSend(MQConfig.queue, msg);
    }*/

   public void sendMiaoshaMessage(MiaoshaMessage mm){
       String msg = RedisService.BeanToString(mm);
       amqpTemplate.convertAndSend(MQConfig.miaosha_queue, msg);
   }
}
