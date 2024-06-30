package com.ooyyh.top.controller;

import com.ooyyh.top.entity.Application;
import com.ooyyh.top.entity.Transaction;
import com.ooyyh.top.service.ApplicationService;
import com.ooyyh.top.service.TransactionService;
import com.ooyyh.top.util.ColorFul;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@Controller
@CrossOrigin
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    ApplicationService applicationService;

    @Autowired
    TransactionService transactionService;

    @GetMapping("/getMyTransaction") //拿到我的所有交易
    @ResponseBody
    public Map getMyTransaction(@RequestHeader String token) throws Exception {
        Map result = transactionService.getMyTransaction(token);
        ColorFul.print(result.toString(), ColorFul.RED);
        return formatOutput(result);
    }

    @PostMapping("/saveTransaction") //保存申请
    @ResponseBody
    public Map saveTransaction(@RequestBody Transaction transaction) throws Exception {
        Map result = transactionService.saveTransaction(transaction);
        ColorFul.print(result.toString(), ColorFul.RED);
        return formatOutput(result);
    }

    @PostMapping("/publishTransaction") //申请上链
    @ResponseBody
    public Map publishTransaction(@RequestBody Transaction transaction) throws Exception {
        Map result = transactionService.publishTransaction(transaction);
        ColorFul.print(result.toString(), ColorFul.RED);
        return formatOutput(result);
    }

    @PostMapping("/acceptTransaction") //买家接受交易
    @ResponseBody
    public Map acceptTransaction(Transaction transaction) throws Exception {
        String tokenTransaction =transaction.getToAccountId();
        String index=transaction.getTranId();
        Map result = transactionService.acceptTransaction(transaction.getToAccountId(),index);
        ColorFul.print(result.toString(), ColorFul.RED);
        return formatOutput(result);
    }
    @GetMapping("/getAllTransactions")
    @ResponseBody
    public Map getAllTransactions() throws UnsupportedEncodingException {
        Map result = transactionService.getAllTransactions();
        ColorFul.print(result.toString(), ColorFul.RED);
        return result;
    }

    @PostMapping("/getTransactionByRecordId")
    @ResponseBody
    public Map getTransactionByRecordId(String recordId) throws UnsupportedEncodingException {
        Map result = transactionService.getTransactionByRecordId(Integer.parseInt(recordId));
        ColorFul.print(result.toString(), ColorFul.RED);
        return result;
    }

//    @GetMapping("/getSocialTransfer") //拿到所有转出
//    @ResponseBody
//    public Map getAllTransfer(@RequestHeader String token) throws Exception {
//        Map result = applicationService.getSocialTransfer(token);
//        ColorFul.print(result.toString(), ColorFul.RED);
//        return formatOutput(result);
//    }

//    @GetMapping("/getMyTransfer") //拿到我的转出
//    @ResponseBody
//    public Map getMyTransfer(String id) throws Exception {
//        Map result = applicationService.getMyTransfer(id);
//        ColorFul.print(result.toString(), ColorFul.RED);
//        return formatOutput(result);
//    }

//    @GetMapping("/approvedTransfer")//同意转出
//    @ResponseBody
//    public Map approvedTransfer(@RequestHeader String token, String index) throws Exception {
//        Map result = applicationService.approvedTransfer(token, index);
//        ColorFul.print(result.toString(), ColorFul.RED);
//        return formatOutput(result);
//    }
//
//    @GetMapping("/acceptTransfer") //接受转入
//    @ResponseBody
//    public Map acceptTransfer(@RequestHeader String token, String index) throws Exception {
//        Map result = applicationService.acceptTransfer(token, index);
//        ColorFul.print(result.toString(), ColorFul.RED);
//        return formatOutput(result);
//    }
//
//    @PostMapping("/saveTransfer") //保存申请
//    @ResponseBody
//    public Map saveTransfer(@RequestBody Application application) throws Exception {
//        Map result = applicationService.saveTransfer(application);
//        ColorFul.print(result.toString(), ColorFul.RED);
//        return formatOutput(result);
//    }
//
//    @PostMapping("/applyTransfer") //发送申请
//    @ResponseBody
//    public Map applyTransfer(@RequestBody Application application) throws Exception {
//        Map result = applicationService.applyTransfer(application);
//        ColorFul.print(result.toString(), ColorFul.RED);
//        return formatOutput(result);
//    }

    private Map formatOutput(Map result) {
        return result;
    }



}
