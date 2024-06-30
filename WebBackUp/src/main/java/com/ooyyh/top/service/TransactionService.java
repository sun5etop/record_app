package com.ooyyh.top.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ooyyh.top.dao.AccountMapper;
import com.ooyyh.top.dao.ApplicationMapper;
import com.ooyyh.top.dao.TransactionMapper;
import com.ooyyh.top.dao.UserMapper;
import com.ooyyh.top.entity.*;
import com.ooyyh.top.util.HttpUtils;
import com.ooyyh.top.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

@Service
public class TransactionService {
    @Autowired
    ApplicationMapper applicationMapper;
    @Autowired
    UserMapper userMapper;

    @Autowired
    TransactionMapper transactionMapper;
    @Autowired
    AccountMapper accountMapper;


    public Map saveTransaction(Transaction transaction){
        if(transaction.getTranId()==null){
            UUID uuid = UUID.randomUUID();
            transaction.setTranId(uuid.toString());
        }
        transaction.setTranStatus(2);
        transactionMapper.insert(transaction);
        return Result.success();
    }


    //提交申请
    public Map<String, Object> publishTransaction(Transaction transaction){
        // 检查交易申请是否存在
        QueryWrapper<Transaction> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tran_id", transaction.getTranId());
        Transaction existingTransaction = transactionMapper.selectOne(queryWrapper);

        if (existingTransaction == null) {
            return Result.error("未找到对应的申请交易记录!");
        }

        // 准备转移请求参数
        List<Object> transactionParams = new ArrayList<>();
        //根据账户id获取账户地址
        QueryWrapper<Account> queryWrapperAc=new QueryWrapper<>();
        queryWrapperAc.eq("account_id",transaction.getFromAccountId());
        Account account = accountMapper.selectOne(queryWrapperAc);

        transactionParams.add(account.getAccountAddress());
        transactionParams.add(transaction.getTranRecordId());


        // 发送转移请求
        JSONObject transferResult = (JSONObject) JSONObject.parse(HttpUtils.commonReq("publishTransaction", transactionParams));
        if (!(Boolean) transferResult.get("statusOK")) {
            return Result.error("提交申请上链失败!");
        } else {
            // 更新申请状态为已申请
            transaction.setTranStatus(0); // "0" 代表在售中
            // 更新申请记录
            transactionMapper.updateById(transaction);
            return Result.success("提交申请上链成功!");
        }
    }



    //买家接受交易,token包含买家的名字？
    public Map acceptTransaction(String token,String tranId) throws UnsupportedEncodingException{
        //查找买家
        String tokenT = URLDecoder.decode(token, "UTF-8");
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("owner_name",tokenT);
        Account buyer=accountMapper.selectOne(queryWrapper);

        //查找交易
        QueryWrapper<Transaction> queryWrapperTran = new QueryWrapper<>();
        queryWrapperTran.eq("tran_id",tranId);
        Transaction transaction=transactionMapper.selectOne(queryWrapperTran);

        //查找卖家的地址
        QueryWrapper<Account> queryWrapperSeller = new QueryWrapper<>();
        queryWrapperSeller .eq("account_id",transaction.getFromAccountId());
        Account seller = accountMapper.selectOne(queryWrapperSeller);

        List funcParam = new ArrayList();
        funcParam.add(seller.getAccountAddress());
        funcParam.add(buyer.getAccountAddress());
        funcParam.add(transaction.getTranRecordId());
        JSONObject data = (JSONObject) JSONObject.parse(HttpUtils.commonReq("approveTransaction",funcParam));
        if (!(Boolean)data.get("statusOK")){
            return Result.error();
        } else {
            QueryWrapper<Transaction> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("tran_status",0);
            Transaction transactionfinish = transactionMapper.selectOne(queryWrapper1);
            transactionfinish.setTranStatus(1);
            transactionfinish.setToAccountId(buyer.getAccountId());
            transactionMapper.update(transactionfinish,queryWrapper1);
            return Result.success();
        }

    }


    //根据recordId获得tranid
    public Map getTransactionByRecordId(int recordId){
        QueryWrapper<Transaction> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tran_record_id",recordId);
        Transaction transaction = transactionMapper.selectOne(queryWrapper);
        if(transaction==null){
            return Result.error("当前唱片未进行交易！");
        } else {
            return Result.success(transaction);
        }
    }




    public Map getMyTransaction(String accountId) throws UnsupportedEncodingException {
        QueryWrapper<Transaction> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("from_account_id",accountId);
        List<Transaction> allAp = transactionMapper.selectList(queryWrapper);
        if (allAp.size() == 0) {
            return Result.error("当前账户没有任何交易！");
        } else {
            return Result.success(allAp);
        }
    }

    public Map getAllTransactions(){
        QueryWrapper<Transaction> queryWrapper = new QueryWrapper<>();
        List<Transaction> transactions = transactionMapper.selectList(queryWrapper);
        return Result.success(transactions);
    }


}
