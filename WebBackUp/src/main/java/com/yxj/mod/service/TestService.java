package com.yxj.mod.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yxj.mod.dao.TestMapper;
import com.yxj.mod.entity.TestEntity;
import com.yxj.mod.util.HttpUtils;
import com.yxj.mod.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

@Service
public class TestService {
    @Autowired
    private TestMapper testMapper;

    public List<TestEntity> getAllTest(){
        return testMapper.selectList(null);
    }
    public Map generateOneYear(String time) {
        Map<String, Object> oneYear = new HashMap<>();
        //time是202406/截取年份
        String year = time.substring(0, 4);
        for (int i = 0; i < 13; i++) {
            oneYear.put(year + i, null);
        }
        return oneYear;
    }

}
