package cn.nowcode.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author
 * @create 2019-07-15 15:55
 */
@Configuration
public class MQConfig {
    public static final String queue = "queue";
    public static final String miaosha_queue = "miaosha.queue";
    @Bean
    public Queue queue(){
        return new Queue(queue,true);
    }
}
