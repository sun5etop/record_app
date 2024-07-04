package com.yxj.mod.controller;

import com.yxj.mod.dao.AccountMapper;
import com.yxj.mod.entity.Account;
import com.yxj.mod.service.AccountService;
import com.yxj.mod.util.ColorFul;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
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
