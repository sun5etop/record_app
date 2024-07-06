package com.yxj.mod.messageQueue;

import com.yxj.mod.service.DropRecordService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

/**
 * 唱片掉落监听器
 * 在这里实现redis->数据库和区块链的同步
 */
@Service
public class RabbitmqListenner implements MessageListener {

    @Autowired
    DropRecordService dropRecordService;

    @Override
    public void onMessage(Message msg) {
        byte[] data = msg.getBody();
        try {
            String 	json = new String(data,"utf-8");
            System.out.println("json here:"+json);
            dropRecordService.saveRecord(json);   //将监听到的订单写入MySQL
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            System.out.println("error");
            e.printStackTrace();
        }
    }
}
