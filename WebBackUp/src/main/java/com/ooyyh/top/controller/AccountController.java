package com.ooyyh.top.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ooyyh.top.dao.AccountMapper;
import com.ooyyh.top.dao.UserMapper;
import com.ooyyh.top.entity.Account;
import com.ooyyh.top.entity.User;
import com.ooyyh.top.service.AccountService;
import com.ooyyh.top.service.UserService;
import com.ooyyh.top.util.ColorFul;
import com.ooyyh.top.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountMapper accountMapper;
    @Autowired
    AccountService accountService;
    @PostMapping(value = "/register")
    @ResponseBody
    public Map register(@RequestBody Account account) {
        Map result = accountService.register(account);
        ColorFul.print(result.toString(),ColorFul.RED);
        return  result;
    }
    @PostMapping(value = "/login")
    @ResponseBody
    public Map login(@RequestBody Account account) {
        Map result = accountService.login(account);
        ColorFul.print(result.toString(),ColorFul.RED);
        return result;
    }

    @GetMapping("/getAccountAllRecords")
    @ResponseBody
    public Map getAccountAllRecord(@RequestHeader String token) throws UnsupportedEncodingException {
        Map result = accountService.getAccountAllRecords(token);
        ColorFul.print(result.toString(), ColorFul.RED);
        return result;
    }

    @GetMapping("/halfRandomDrop")
    @ResponseBody
    public Map halfRandomDrop(@RequestHeader String token) throws UnsupportedEncodingException {
        Map result = accountService.halfRandomDrop(token);
        ColorFul.print(result.toString(), ColorFul.RED);
        return result;
    }

    @GetMapping("/getAllAccounts")
    @ResponseBody
    public Map getAllAccounts() throws UnsupportedEncodingException {
        Map result = accountService.getAllAccounts();
        ColorFul.print(result.toString(), ColorFul.RED);
        return result;
    }

    @GetMapping("/getAllRecords")
    @ResponseBody
    public Map getAllRecords() throws UnsupportedEncodingException {
        Map result = accountService.getAllRecords();
        ColorFul.print(result.toString(), ColorFul.RED);
        return result;
    }

}
