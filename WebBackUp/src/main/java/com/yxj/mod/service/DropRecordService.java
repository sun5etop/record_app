package com.yxj.mod.service;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yxj.mod.dao.AccountMapper;
import com.yxj.mod.dao.RecordMapper;
import com.yxj.mod.entity.Account;
import com.yxj.mod.entity.Record;
import com.yxj.mod.messageQueue.RabbitmqSendMessage;
import com.yxj.mod.util.HttpUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 该服务是为账户掉落添加唱片，考虑到高并发的情况，
 * 参考秒杀系统，用了redis和rabbitMQ消息队列
 */
@Service
public class DropRecordService{
    @Autowired
    SecurityService securityService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    RabbitmqSendMessage rsm;


    @Autowired
    RecordMapper recordMapper;

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    RedisService redisService;

    public Map pbDrop(String utoken){
        //先根据token取出用户信息
        String jsonAccount = (String)redisService.getValue(utoken);
        Account account=JSONUtil.toBean(jsonAccount,Account.class);

        // 生成一个 0.0 到 1.0 之间的随机数
        Random random = new Random();
        double randomValue = random.nextDouble();

        Map result = new HashMap();
        if(randomValue<0.999){
            String jsonRecord=DropRecord(account.getAccountId());
            if(jsonRecord.charAt(0)=='{') {
                Record record = JSONUtil.toBean(jsonRecord, Record.class);

                result.put("code", "200");
                result.put("msg", "成功掉落唱片，id：" + record.getRecordId());
                result.put("data", record.getRecordType());
            }
            else{
                result.put("code", "201");
                result.put("msg", jsonRecord);
                result.put("data", null);
            }
        }else{
            result.put("code", "201");
            result.put("msg", "没有掉落唱片~");
            result.put("data", null);
        }
        return result;
    }

    /**
     * 每次登录的时候调用该方法
     * 初始化 ，将mysql中可掉落的唱片信息缓存到redis中
     * @return
     */
    public List<Record> loadRecords() {
//        List<Record> list = redisService.getList("Records");
//        if(list.isEmpty()) {

            int maxId=securityService.getMaxDropId();
            QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
            queryWrapper.gt("record_id",maxId-1);
            List<Record>  list = recordMapper.selectList(queryWrapper);
            redisService.setList("Records",list);
//        }
        return list;
    }

    /**
     * redis中删去该唱片
     * @param id
     */
    public void decreaseRecord(String id) {
        int recordID = Integer.parseInt(id);
        List<Record> list = redisService.getList("Records");
        if (list!=null)
        {
            for (Record record : list)
            {
                if (recordID==record.getRecordId())
                {
                    list.remove(record);
                    //写回redis
                    redisService.setListExpire("Records", list, 60*30,TimeUnit.SECONDS);
                    return ;
                }
            }
        }
    }

    /**
     * 根据唱片编号找到对应的唱片
     * @param Sid
     * @return
     */
    public Record findRecord(String Sid) {
        List<Record> list = redisService.getList("Records");
        int id = Integer.parseInt(Sid);
        for(Record record:list) {
            if(record.getRecordId()==id) {
                return record;
            }
        }
        return null;
    }

    /**
     * 唱片掉落
     * @param
     * @param accountId
     * @return
     */
    public String DropRecord(String accountId) {
        //对Records上锁
        RLock rLock = redissonClient.getLock("lock");
        rLock.lock();

        int recordId=securityService.getMaxDropId();
        String sid= String.valueOf(recordId);

        String key = accountId + ":" + sid;

        Long value = (Long) redisService.getValue(key);
        if (value != null) {
            return "已有该次掉落记录！";
        }


        Record record = findRecord(sid);
        if (record!=null) {

            //redis删除
            decreaseRecord(sid);

            //redis设置key的值
            redisService.setValueWithExpiry(key, System.currentTimeMillis(), 60*30,TimeUnit.SECONDS);

            //唱片设置拥有者id
            record.setRecordOwnerId(accountId);

            String json = JSONUtil.toJsonStr(record);

            rsm.send(json); // 异步上链
            rLock.unlock(); // 解锁

            return String.valueOf(json);

        } else {
            rLock.unlock();
            return "failed,Record==null!";
        }
    }

    /**
     * 将redis信息同步到mysql和webase
     * @param json
     */
    public void saveRecord(String json) {
        //更新数据库
        securityService.plusDropId();
        Record record = JSON.parseObject(json, Record.class);
        recordMapper.updateById(record);

        //根据record的拥有者id查找到人的账户地址
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account_id",record.getRecordOwnerId());
        Account owner = accountMapper.selectOne(queryWrapper);
        String ownerAddress=owner.getAccountAddress();

        //信息同步上链
        //智能合约添加唱片
        List funcParams = new ArrayList();
        funcParams.add(ownerAddress);
        funcParams.add(record.getRecordId());

        JSONObject response = (JSONObject) JSONObject.parse(HttpUtils.commonReq("addRecordToAccount",funcParams));

    }


}
