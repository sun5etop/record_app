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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 该服务是为账户掉落添加唱片，考虑到高并发的情况，
 * 参考秒杀系统，用了redis和rabbitMQ消息队列
 */
@Service
public class DropRecordService {
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


    /**
     * 初始化 ，将mysql中的唱片信息缓存到redis中
     * @return
     */
    public List<Record> querySeckill() {
        List<Record> list = (List<Record>) redisService.getValue("Records");
        if(list==null) {
            list = recordMapper.selectList(null);
            redisService.setValueWithExpiry("Records", list, 60*30, TimeUnit.SECONDS);
        }
        return list;
    }

    /**
     * redis中删去该唱片
     * @param id
     */
    public void decreaseRecord(String id) {
        int recordID = Integer.parseInt(id);
        List<Record> list =   (List<Record>) redisService.getValue("Records");
        if (list!=null)
        {
            for (Record record : list)
            {
                if (recordID==record.getRecordId())
                {
                    list.remove(record);
                    //写回redis
                    redisService.setValueWithExpiry("Records", list, 60*30,TimeUnit.SECONDS);
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
        List<Record> list =   (List<Record>) redisService.getValue("Records");
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
     * @param sid
     * @param accountId
     * @return
     */
    public String DropRecord(String sid, String accountId) {
        String key = accountId + ":" + sid;
        int recordId = Integer.parseInt(sid);

        Long value = (Long) redisService.getValue(key);
        if (value != null) {
            return "exist";
        }

        //对Records上锁
        RLock rLock = redissonClient.getLock("Records");
        rLock.lock();

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

            return "success";

        } else {
            rLock.unlock();
            return "failed";
        }
    }

    /**
     * 将redis信息同步到mysql和webase
     * @param json
     */
    public void saveRecord(String json) {
        //更新数据库
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

        //最后递增掉落id
        securityService.plusDropId();
    }


}
