package com.haozhi.item.dao;

import com.haozhi.item.pojo.HzYw;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author kgy
 * @version 1.0
 * @date 2019/12/27 10:21
 */
public interface HzYwRepository extends Mapper<HzYw> {

    @Select("select zid from haozhi_yw where id = #{oneId}")
    HzYw queryGYById(String oneId);

    @Select("select * FROM haozhi_yw WHERE zid=#{id} AND sj_name=#{sId}")
    List softListById(@Param("id") String id,@Param("sId") String sId);
}
