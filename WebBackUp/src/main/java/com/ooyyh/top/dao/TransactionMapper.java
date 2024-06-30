package com.ooyyh.top.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ooyyh.top.entity.Record;
import com.ooyyh.top.entity.Transaction;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TransactionMapper extends BaseMapper<Transaction> {

}
