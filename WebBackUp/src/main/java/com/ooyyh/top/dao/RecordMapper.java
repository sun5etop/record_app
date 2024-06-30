package com.ooyyh.top.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ooyyh.top.entity.Account;
import com.ooyyh.top.entity.Record;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecordMapper extends BaseMapper<Record> {
}
