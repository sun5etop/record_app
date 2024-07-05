package com.yxj.mod.controller;

import com.yxj.mod.entity.TestEntity;
import com.yxj.mod.service.TestService;
import com.yxj.mod.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin
public class TestController {
    @Autowired
    private TestService testService;
    @RequestMapping("/test")
    @ResponseBody
    public List<TestEntity> test() {
        return testService.getAllTest();
    }
    @GetMapping("/test1")
    @ResponseBody
    public Map test1()
    {
        return Result.success();
    }
    @GetMapping("/test2")
    @ResponseBody
    public Map test2()
    {
        return testService.generateOneYear("2022");
    }

}
