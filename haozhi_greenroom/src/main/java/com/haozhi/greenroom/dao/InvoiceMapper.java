package com.haozhi.greenroom.dao;

import com.haozhi.greenroom.pojo.Invoice;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/14 15:11
 */
public interface InvoiceMapper extends Mapper<Invoice> {
    @Select("select * from haozhi_invoice where time >= #{time1} and time <= #{time2} order by time desc")
    List<Invoice> selectByTime1AndTime2(@Param("time1") String time1, @Param("time2") String time2);
}
