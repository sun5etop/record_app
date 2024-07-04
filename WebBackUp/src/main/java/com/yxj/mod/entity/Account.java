package com.yxj.mod.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("account")
public class Account {
    @TableField("owner_name")
    private String ownerName;
    @TableField("account_address")
    private String accountAddress;

    @TableId("account_id")
    private String accountId;

    @TableField("account_type")
    private String accountType;

    @TableField("password")
    private String password;

    public Account(){

    }

    public Account(String id, String name, String pw,String type) {
        this.accountId=id;
        this.ownerName=name;
        this.password=pw;
        this.accountType=type;
    }
}

