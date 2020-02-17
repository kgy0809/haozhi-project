package com.haozhi.greenroom.dao;

import com.haozhi.greenroom.pojo.Account;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/8 13:43
 */
public interface AccountMapper extends Mapper<Account> {
    @Select("select * from haozhi_account where time >= #{time1} and time <= #{time2} order by time desc")
    List<Account> selectByTime1AndTime2(@Param("time1") String time1, @Param("time2") String time2);
}
