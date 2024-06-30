package com.ooyyh.top.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ooyyh.top.entity.Account;
import com.ooyyh.top.entity.Application;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountMapper extends BaseMapper<Account> {
}
