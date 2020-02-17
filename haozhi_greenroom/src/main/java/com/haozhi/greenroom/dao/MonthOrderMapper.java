package com.haozhi.greenroom.dao;

import com.haozhi.greenroom.pojo.MonthOrder;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/18 16:26
 */
public interface MonthOrderMapper extends Mapper<MonthOrder> {
    @Select("select * from haozhi_month_order where time >= #{time1} and time <= #{time2} order by time desc")
    List<MonthOrder> selectByTimeAndTime2(String time1, String time2);
}
