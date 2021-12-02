//package tech.snnukf.seckillsys.config;
//
//import com.rabbitmq.client.AMQP;
//import org.springframework.amqp.core.Binding;
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.FanoutExchange;
//import org.springframework.amqp.core.Queue;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @author simple.jbx
// * @ClassName RabbitMQConfig
// * @description RabbitMQ 配置类
// * @email jb.xue@qq.com
// * @github https://github.com/simple-jbx
// * @date 2021/11/03/ 20:24
// */
//@Configuration
//public class RabbitMQFanoutConfig {
//
//    private static final String QUEUE01 = "queue_fanout01";
//    private static final String QUEUE02 = "queue_fanout02";
//    private static final String EXCHANGE = "fanoutExchange";
//
//
//    @Bean
//    public Queue queue() {
//        return new Queue("queue", true);
//    }
//
//    @Bean
//    public Queue queue01() {
//        return new Queue(QUEUE01);
//    }
//
//    @Bean
//    public Queue queue02() {
//        return new Queue(QUEUE02);
//    }
//
//    @Bean
//    public FanoutExchange fanoutExchange() {
//        return new FanoutExchange(EXCHANGE);
//    }
//
//    @Bean
//    public Binding binding01() {
//        return BindingBuilder.bind(queue01()).to(fanoutExchange());
//    }
//
//    @Bean
//    public Binding binding02() {
//        return BindingBuilder.bind((queue02())).to(fanoutExchange());
//    }
//}
