package com.yxj.mod.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yxj.mod.dao.AccountMapper;
import com.yxj.mod.dao.RecordMapper;
import com.yxj.mod.entity.Account;
import com.yxj.mod.entity.Record;
import com.yxj.mod.util.HttpUtils;
import com.yxj.mod.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class AccountService {

    @Autowired
    SecurityService securityService;

    @Autowired
    RecordMapper recordMapper;

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    RedisService redisService;

    /**
     * 用户注册
     * @param account
     * @return
     */
    public Map register(Account account) {
        // switch account.getUserType 1账户 2掉落器 3交易器
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("owner_name", account.getOwnerName());
        if (accountMapper.selectOne(queryWrapper) != null) {
            Map result = new HashMap();
            result.put("code", "500");
            result.put("msg", "该账号名已存在");
            result.put("data", null);
            return result;
        }
        List funcParam = new ArrayList();
        if ("1".equals(account.getAccountType())) {
            String createPri=HttpUtils.createPrivateKey(account.getOwnerName());
            //to do 解析返回包1，获取用户私钥
            String privateKey=createPri.substring(12,54);
            account.setAccountAddress(privateKey);
            funcParam.add(privateKey);
            funcParam.add(account.getOwnerName());
            funcParam.add(account.getAccountId());

            String data = HttpUtils.commonReq("registerAccount", funcParam);
            JSONObject responseData = (JSONObject) JSONObject.parse(data);
//            System.out.println(responseData.get("transactionIndex"));
            Map result = new HashMap();
            if (!(Boolean) responseData.get("statusOK")) {
                result.put("code", "500");
                result.put("msg", responseData.get("message"));
                result.put("data", null);
                return result;
            } else {
                result.put("code", "200");
                result.put("msg", "注册成功");
                result.put("data", null);
                accountMapper.insert(account);
                return result;
            }


        } else if ("2".equals(account.getAccountType())) {
            //todo
            String data = HttpUtils.commonReq("regLaodongRoles", funcParam);
            JSONObject responseData = (JSONObject) JSONObject.parse(data);
            Map result = new HashMap();
            if (!(Boolean) responseData.get("statusOK")) {
                result.put("code", "500");
                result.put("msg", responseData.get("message"));
                result.put("data", null);
                return result;
            } else {
                result.put("code", "200");
                result.put("msg", "注册成功");
                result.put("data", null);
                accountMapper.insert(account);
                return result;
            }
        } else if ("3".equals(account.getAccountType())) {
            //todo
            String data = HttpUtils.commonReq("addSocialSecDept", funcParam);
            JSONObject responseData = (JSONObject) JSONObject.parse(data);
            Map result = new HashMap();
            if (!(Boolean) responseData.get("statusOK")) {
                result.put("code", "500");
                result.put("msg", responseData.get("message"));
                result.put("data", null);
                return result;
            } else {
                result.put("code", "200");
                result.put("msg", "注册成功");
                result.put("data", null);
                accountMapper.insert(account);
                return result;
            }
        } else {
            // 处理未知的用户类型
            throw new IllegalArgumentException("未知的用户类型: " + account.getAccountType());
        }


    }


    /**
     * 用户登录
     * @param account
     * @return
     */
    public Map login(Account account) {
        Map result = new HashMap();
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account_id", account.getAccountId()); //首先有这个人
        Account info = accountMapper.selectOne(queryWrapper);

        if (info != null) {
            if (info.getAccountType().equals("1")) {
                if (info.getPassword().equals(account.getPassword())) {
                    result.put("code", "200");
                    result.put("msg", "登陆成功："+info.getOwnerName());
                    result.put("data", info);

                    String token = UUID.randomUUID().toString();

                    //存到redis，并设置30分钟过期时间
                    //RedisUtils.INSTANCE.set(token, JSON.toJSONString(info),60*30 );
                    redisService.setValueWithExpiry(token, JSON.toJSONString(info),60*30, TimeUnit.SECONDS);

                    result.put("uToken", token);
                    return result;
                } else {
                    result.put("code", "500");
                    result.put("msg", "密码错误");
                    result.put("data", null);
                    return result;
                }
            } else {
                result.put("code", "201");
                result.put("msg", info.getOwnerName());
                result.put("data", info);
                return result;
            }
        } else {
            result.put("code", "500");
            result.put("msg", "用户不存在,请检查账户id和密码！");
            result.put("data", null);
            return result;
        }


    }
    public Map getAccountAllRecords(String token)throws UnsupportedEncodingException{
        //根据用户名获取account id
        String tokenT = URLDecoder.decode(token, "UTF-8");
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account_id",tokenT);
        Account account = accountMapper.selectOne(queryWrapper);
        //根据用户id获取用户所拥有的record_id
        QueryWrapper<Record> queryWrapperRc = new QueryWrapper<>();
        queryWrapperRc.eq("record_owner_id",account.getAccountId());
        List<Record> records = recordMapper.selectList(queryWrapperRc);
        return Result.success(records);
    }


    /**
     * 为账户掉落唱片
     * @param token
     * @return
     * @throws UnsupportedEncodingException
     */
    public Map addAccountRecord(String token) throws UnsupportedEncodingException {
        int _recordId=securityService.getMaxDropId();

        String tokenT = URLDecoder.decode(token, "UTF-8");
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account_id",tokenT);
        Account account = accountMapper.selectOne(queryWrapper);


        QueryWrapper<Record> queryWrapperRc = new QueryWrapper<>();
        queryWrapperRc.eq("record_id",_recordId);
        Record record = recordMapper.selectOne(queryWrapperRc);
        record.setRecordOwnerId(account.getAccountId());
        recordMapper.updateById(record);

        //智能合约添加唱片
        List funcParams = new ArrayList();
        funcParams.add(account.getAccountAddress());
        funcParams.add(record.getRecordId());
        

        JSONObject response = (JSONObject) JSONObject.parse(HttpUtils.commonReq("addRecordToAccount",funcParams));


        if(!(Boolean)response.get("statusOK")){
            return Result.error();
        }else{
            //递增掉落id
            securityService.plusDropId();
            return Result.success(record.getRecordType());
        }
    }
    public Map halfRandomDrop(String token)throws UnsupportedEncodingException{
        int available= securityService.getMaxRecordId()-securityService.getMaxDropId();
        //bool
        Random random = new Random();
        // 生成一个 0.0 到 1.0 之间的随机数
        double randomValue = random.nextDouble();
        if(randomValue<0.300){
            return addAccountRecord(token);
        }else{
            Map result = new HashMap();
            result.put("code", "201");
            result.put("msg", "没有掉落唱片~");
            result.put("data", null);
            return result;
        }
    }

    /**
     * 获得所有的账户
     * @return
     */
    public Map getAllAccounts(){
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account_type","1");
        List<Account> accounts = accountMapper.selectList(queryWrapper);
        return Result.success(accounts);
    }

    /**
     * 获得所有的唱片
     * @return
     */
    public Map getAllRecords(){
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        List<Record> records = recordMapper.selectList(queryWrapper);
        return Result.success(records);
    }
}
