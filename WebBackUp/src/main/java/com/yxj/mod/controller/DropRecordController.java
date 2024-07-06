package com.yxj.mod.controller;

import com.yxj.mod.service.DropRecordService;
import com.yxj.mod.util.ColorFul;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping("/drop")
public class DropRecordController {
    @Autowired
    DropRecordService dropRecordService;

    @GetMapping("/dropRecord")
    @ResponseBody
    public Map dropRecord(@RequestHeader String utoken) throws UnsupportedEncodingException {
        Map result = dropRecordService.pbDrop(utoken);
        ColorFul.print(result.toString(), ColorFul.RED);
        return result;
    }
}
