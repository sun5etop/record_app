package com.yxj.mod.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("recordInfo")
@Data
public class RecordInfo {
    @TableId("record_type")
    int recordType;

    @TableField("record_name")
    String recordName;

}
