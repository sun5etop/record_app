package com.yxj.mod.messageQueue;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitmqSendMessage {

    @Autowired
    RabbitTemplate rabbitTemplate;

    private final String QUEEN_NAME = "myQueue";

    public static final String EXCHANGE_NAME = "myExchange";
    public static final String ROUTING_KEY = "myRoutingKey";

    /**
     * 发送消息
     * @param msg
     */
    public void send(String msg)
    {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME,ROUTING_KEY,msg);
    }

}
