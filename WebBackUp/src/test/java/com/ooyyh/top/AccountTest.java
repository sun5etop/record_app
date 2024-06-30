package com.ooyyh.top;

import com.ooyyh.top.controller.AccountController;
import com.ooyyh.top.entity.Account;
import com.ooyyh.top.entity.Record;
import com.ooyyh.top.entity.Transaction;
import com.ooyyh.top.service.AccountService;
import com.ooyyh.top.service.SecurityService;
import com.ooyyh.top.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.util.Map;

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
        int publishNum = 100;
        int recordType =1;
        securityService.addRecords(100,1);
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
}
