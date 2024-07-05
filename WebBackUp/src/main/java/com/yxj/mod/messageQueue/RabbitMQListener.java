package com.yxj.mod.messageQueue;

import com.yxj.mod.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListener {

    private final Receiver receiver;

    public RabbitMQListener(Receiver receiver) {
        this.receiver = receiver;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void listen(String message) {
        receiver.receiveMessage(message);
    }
}
