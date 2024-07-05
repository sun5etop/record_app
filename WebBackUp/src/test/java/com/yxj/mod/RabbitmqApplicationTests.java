package com.yxj.mod;

//import com.yxj.mod.config.RabbitMQConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class RabbitmqApplicationTests {
//    @Autowired
//    RabbitTemplate rabbitTemplate;

    public void testSend() {
//        String message = "Hello, RabbitMQ!";
//        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, message);
        System.out.println("Message sent successfully!");
    }
}
