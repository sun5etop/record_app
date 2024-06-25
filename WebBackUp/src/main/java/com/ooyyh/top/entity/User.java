package com.ooyyh.top.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user")
public class User {
    //---公共参数---
    @TableField("user_type")
    private String userType;
    @TableId("username")
    private String username;
    private String password;
    private String city;
    //养老保险账号参数
    private String id;
    //---劳动局参数---
    @TableField("address")
    private String address;
    @TableField("max_base")
    private String maxBase;
    @TableField("min_base")
    private String minBase;
    @TableField("personal_rate")
    private String personalRate;
    @TableField("company_rate")
    private String companyRate;
}
