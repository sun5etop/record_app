package com.yxj.mod.messageQueue;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class RabbitmqListenner implements MessageListener {

    //@Autowired
    //private SeckillService ss;

    @Override
    public void onMessage(Message msg) {
        byte[] data = msg.getBody();
        try {
            String 	json = new String(data,"utf-8");
            System.out.println(json);
            //ss.saveOrder(json);   //将监听到的订单写入MySQL
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
