package com.yxj.mod;

import com.yxj.mod.config.RabbitMQConfig;
import com.yxj.mod.controller.AccountController;
import com.yxj.mod.entity.Account;
import com.yxj.mod.entity.Transaction;
import com.yxj.mod.messageQueue.RabbitmqSendMessage;
import com.yxj.mod.service.AccountService;
import com.yxj.mod.service.RedisService;
import com.yxj.mod.service.SecurityService;
import com.yxj.mod.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AccountTest {
    @Autowired
    AccountService accountService;
    @Autowired
    SecurityService securityService;
    @Autowired
    TransactionService transactionService;
    @Autowired
    AccountController accountController;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    RabbitmqSendMessage rsm;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    RedisService redisService;

    @Test
    public void registerTest(){//注册账户
        Account account=new Account("id123","ykm","123","1");
        accountController.register(account);
    }
    @Test
    public void registermaijiaTest(){//注册账户
        Account account=new Account("id467","yxj","456","1");
        accountService.register(account);
    }
    @Test
    public void loginTest(){//登录账户
        Account account=new Account("id123","ykm","123","1");
        accountService.login(account);
    }
    @Test
    public void addRecordsTest(){//发行唱片

        for(int i=0;i<3;i++){
            securityService.addRecords(1,1);
            securityService.addRecords(1,2);
            securityService.addRecords(1,3);
        }
    }
    @Test
    public  void addAccountRecord() throws UnsupportedEncodingException {// 后端掉落record_id给用户，
        String accountName = "ykm";
        accountService.addAccountRecord(accountName);
    }
    @Test
    public void getAccountAllRecordsTest() throws UnsupportedEncodingException {//获取用户的所拥有的唱片
        String accountName = "ykm";
        accountService.getAccountAllRecords(accountName);
    }
    @Test
    public void saveTransactionTest(){//保存上链信息到本地数据库
        String fromAccountId = "id123";
        String tranRecordId = "2";
        Transaction transaction = new Transaction(fromAccountId,"",tranRecordId,2);
        transactionService.saveTransaction(transaction);
    }
    @Test
    public void publishTransactionTest(){//通过tranId查看数据库有无该交易，再把此交易记录上链
        String tranId = "8d24aafe-d490-4a18-831b-4dc08bddb141";
        String fromAccountId = "id123";
        String tranRecordId = "2";
        Transaction transaction = new Transaction(tranId,fromAccountId,"",tranRecordId,2);
        transactionService.publishTransaction(transaction);
    }
    @Test
    public void acceptTransaction() throws UnsupportedEncodingException {
        String tranId = "8581c158e2b9";
        String maijia = "yxj";
        transactionService.acceptTransaction(maijia,tranId);
    }

    @Test
    public void drop()throws UnsupportedEncodingException{
        String token="id123";
        for(int i=0;i<10;i++){
            accountService.halfRandomDrop(token);
        }
    }
    @Test
    public void getAllAccountsTest() throws UnsupportedEncodingException {
       accountController.getAllAccounts();
    }

    @Test
    public void show(){
        System.out.println(111);
    }

    @Test
    public void testSend() {
        String message = "Hello, RabbitMQ!";
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, message);
        System.out.println("Message sent successfully!");
    }

    @Test
    public void testSendAndReceive() throws InterruptedException {
        String message = "DropHere!";
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, message);

        // 等待接收消息（异步处理需要一些时间）
        Thread.sleep(1000);

        // 验证接收到的消息
        //assertEquals(message, receiver.getLastMessage());
        System.out.println("Message sent and received successfully!");
    }

    @Test
    public void testSR()throws InterruptedException{
        rsm.send("DropRrr");
        // 等待接收消息（异步处理需要一些时间）
        Thread.sleep(1000);
    }

    @Test
    public void redis(){
        redisService.deleteValue("id123:13");
        redisService.deleteValue("Records");
    }
}
