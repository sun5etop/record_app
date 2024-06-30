package com.ooyyh.top.service;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ooyyh.top.dao.RecordInfoMapper;
import com.ooyyh.top.dao.RecordMapper;
import com.ooyyh.top.dao.SecurityMapper;
import com.ooyyh.top.entity.*;
import com.ooyyh.top.util.HttpUtils;
import com.ooyyh.top.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SecurityService implements CommandLineRunner {
    private static int maxRecordId;

    private static int maxDropId;

    @Autowired
    SecurityMapper securityMapper;

    @Autowired
    RecordMapper recordMapper;

    @Autowired
    RecordInfoMapper recordInfoMapper;

    public int getMaxRecordId(){
        return maxRecordId;
    }
    public int getMaxDropId(){
        return maxDropId;
    }
    public void plusDropId(){
        maxDropId++;
    }

    public Map addRecords(int num, int type) {
        Map result = new HashMap();
        //唱片发行日期
        String date= DateUtil.date().toDateStr();
        QueryWrapper<RecordInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("record_type", type);
        if(recordInfoMapper.selectOne(queryWrapper) == null){
            result.put("code", "500");
            result.put("msg", "没有该唱片类型！");
            result.put("data", null);
            return result;
        }

        List funcParam = new ArrayList();
        funcParam.add(num);
        funcParam.add(type);
        funcParam.add(date);
        //发行专辑成功，内部的recordIndex全局变量改变
        //JSONObject data = (JSONObject) JSONObject.parse(HttpUtils.commonReq("addRecords",funcParam));
        String data = HttpUtils.commonReq("addRecords",funcParam);

        List funcParamGetIndex =new ArrayList();
        String retdata =HttpUtils.commonReq("getNowrecordIndex",funcParamGetIndex);
        String temp = retdata.substring(2, retdata.length() - 2);
        int recordIndex = Integer.parseInt(temp);
        maxRecordId=recordIndex;

        for(int id = recordIndex-num;id<=recordIndex;id++){
            Record record = new Record(id, type, date, "");
            recordMapper.insert(record);

        }

        result.put("code","200");
        result.put("msg","添加唱片成功~");
        result.put("data",null);

        return result;

    }

    public Map addPerson(String userAddress, Person person) {
        Map result = new HashMap();
        List funcParam = new ArrayList();
        funcParam.add(person.getId());
        funcParam.add(person.getAge());
        funcParam.add(person.getName());
        JSONObject data = (JSONObject) JSONObject.parse(HttpUtils.commonReq(userAddress,"addPerson",funcParam));
        if (!(Boolean)data.get("statusOK")) {
            result.put("code","500");
            result.put("msg",data.get("message"));
            result.put("data",null);
            return result;
        } else  {
            result.put("code","200");
            result.put("msg","添加成功");
            result.put("data",null);
            securityMapper.insert(person);
            return result;
        }
    }

    @Override
    public void run(String... args) throws Exception {
        //初始化已有唱片id最大值
        List funcParamGetIndex =new ArrayList();
        String retdata =HttpUtils.commonReq("getNowrecordIndex",funcParamGetIndex);
        String temp = retdata.substring(2, retdata.length() - 2);
        int recordIndex = Integer.parseInt(temp);
        maxRecordId=recordIndex;

        //初始化已掉落id最大值
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("record_owner_id","");
        List<Record> records = recordMapper.selectList(queryWrapper);
        int minId = Integer.MAX_VALUE;
        for (Record record : records) {
            if (record.getRecordId() < minId) {
                minId = record.getRecordId();
            }
        }
        maxDropId=minId;

    }
//    public List<Person> getAllPerson(String userAddress) {
//        List<Person> userList = new ArrayList<>();
//        List<Object> funcParam = new ArrayList<>();
//        String data = HttpUtils.commonReq(userAddress, "getAllPersonId", funcParam);
//        System.out.println(data); // data => ["[1,2]"]
//
//        // 去掉外层的 [" 和 "]
//        if (data.startsWith("[\"") && data.endsWith("\"]")) {
//            data = data.substring(2, data.length() - 2);
//        }
//
//        // 去掉内部的方括号并拆分字符串
//        data = data.replaceAll("[\\[\\]]", "").trim();
//        String[] elements = data.split(",");
//
//        // 将字符串数组转换为整数列表
//        List<Integer> idList = new ArrayList<>();
//        for (String element : elements) {
//            try {
//                idList.add(Integer.parseInt(element.trim()));
//            } catch (NumberFormatException e) {
//                e.printStackTrace();
//            }
//        }
//
//        System.out.println(idList); // 输出 idList 以确保正确解析
//
//        // 使用 idList 进行查询并填充 userList
//        for (Integer id : idList) {
//            QueryWrapper<Person> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("id", id);
//            Person person = securityMapper.selectOne(queryWrapper);
//            userList.add(person);
//        }
//
//        return userList;
//    }

//    public Map getAllRecord() {
//        Map result = new HashMap();
//        List<Record> userList = new ArrayList<Record>();
//
////        System.out.println(idList.get(0));
//        for (int i = 0; i < idList.size(); i++) {
////            System.out.println(i);
//            QueryWrapper<Person> queryWrapper = new QueryWrapper<Person>();
//            queryWrapper.eq("id",idList.get(i));
//            Person onUser = securityMapper.selectOne(queryWrapper);
//            userList.add(onUser);
//        }
//        result.put("code","200");
//        result.put("msg","查询成功");
//        result.put("data",userList);
//        return result;
//
//    }
}
