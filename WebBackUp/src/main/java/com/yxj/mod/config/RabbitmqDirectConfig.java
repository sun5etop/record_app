//package com.yxj.mod.config;
//
//import org.springframework.amqp.core.Binding;
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.DirectExchange;
//import org.springframework.amqp.core.Queue;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RabbitmqDirectConfig {
//    public final static String DIRECTNAME = "weiyh-direct";
//
//    @Bean
//    Queue queue() {
//        // 队列名称，用于接收者监听
//        return new Queue("queue-name");
//    }
//
//    @Bean
//    DirectExchange directExchange() {
//        // 消息名称，重启后是否有效，长期未用是否删除
//        return new DirectExchange(DIRECTNAME, true, false);
//    }
//
//    @Bean
//    Binding binding() {
//        // 将队列以direct的方式进行绑定
//        return BindingBuilder.bind(queue()).to(directExchange()).with("direct");
//    }
//}
