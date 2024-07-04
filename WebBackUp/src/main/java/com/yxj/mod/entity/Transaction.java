package com.yxj.mod.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("transaction")
public class Transaction {
    @TableId("tran_id")
    String tranId;
    @TableField("from_account_id")
    String fromAccountId;
    @TableField("to_account_id")
    String toAccountId;
    @TableField("tran_record_id")
    String tranRecordId;

    @TableField("tran_status")
    int tranStatus;//0-在售 1-已售 2-未上链

    public Transaction(){

    }
    public Transaction( String fromAccountId, String toAccountId, String tranRecordId, int status) {
        this.fromAccountId=fromAccountId;
        this.toAccountId=toAccountId;
        this.tranRecordId=tranRecordId;
        this.tranStatus=status;
    }
    public Transaction( String tranId,String fromAccountId, String toAccountId, String tranRecordId, int status) {
        this.tranId=tranId;
        this.fromAccountId=fromAccountId;
        this.toAccountId=toAccountId;
        this.tranRecordId=tranRecordId;
        this.tranStatus=status;
    }
}
