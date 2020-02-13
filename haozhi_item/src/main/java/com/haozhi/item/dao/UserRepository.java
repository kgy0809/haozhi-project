package com.haozhi.item.dao;

import com.haozhi.item.pojo.User;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author kgy
 * @version 1.0
 * @date 2019/12/27 14:28
 */
public interface UserRepository extends Mapper<User> {

    @SelectProvider(type = PersonDynamicSqlProvider.class, method = "select")
    List<User> findByNameAndAge(Map<String, Object> map);

}
