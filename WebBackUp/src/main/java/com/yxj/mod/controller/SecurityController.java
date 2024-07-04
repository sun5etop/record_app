package com.yxj.mod.controller;

import com.yxj.mod.entity.Person;
import com.yxj.mod.service.SecurityService;
import com.yxj.mod.util.ColorFul;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping("/security")
public class SecurityController {
    @Autowired
    SecurityService securityService;
//    @PostMapping(value = "/addPerson")
//    @ResponseBody
//    public Map addPerson(@RequestHeader String userAddress, @RequestBody Person person){
//        return securityService.addPerson(userAddress,person);
//    }
//    @GetMapping(value = "/getAllPerson")
//    @ResponseBody
//    public Map getAllPerson(@RequestHeader String userAddress){
//        return securityService.getAllPerson(userAddress);
//    }
    @PostMapping(value = "/addPerson")
    @ResponseBody
    public Map addPerson(@RequestHeader String userAddress, @RequestBody Person person){
        Map result = securityService.addPerson(userAddress, person);
        ColorFul.print(result.toString(), ColorFul.RED);
        return result;
    }

//    @GetMapping(value = "/getAllPerson")
//    @ResponseBody
//    public Map getAllPerson(@RequestHeader String userAddress){
//        Map result = securityService.getAllPerson(userAddress);
//        ColorFul.print(result.toString(), ColorFul.RED);
//        return result;
//    }

}
