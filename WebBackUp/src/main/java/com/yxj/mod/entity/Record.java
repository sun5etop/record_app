package com.yxj.mod.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("record")
@Data
public class Record {
    @TableId("record_id")
    int recordId;

    @TableField("record_type")
    int recordType;

    @TableField("publish_time")
    String publishTime;

    @TableField("record_owner_id")
    String recordOwnerId;
    public Record(int id, int type, String date,String recordOwnerId) {
        this.recordId=id;
        this.recordType=type;
        this.publishTime=date;
        this.recordOwnerId=recordOwnerId;
    }

    public Record(){

    }
}
