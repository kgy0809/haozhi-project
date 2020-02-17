package com.haozhi.greenroom.dao;

import com.haozhi.greenroom.pojo.VipOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface VipOrderMapper extends Mapper<VipOrder> {
    @Select("select * from haozhi_vip_order where time >= #{time1} and time <= #{time2} and state = '1' order by time desc")
    List<VipOrder> selectByTime1AndTime2(@Param("time1") String time1, @Param("time2") String time2);
}
