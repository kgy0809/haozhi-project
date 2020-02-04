package com.haozhi.greenroom.service;

import com.haozhi.greenroom.dao.UserMapper;
import com.haozhi.greenroom.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/6 14:01
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User selectUser(String userName) {
        if (userName == null) {
            return null;
        }
        User user = new User();
        user.setUsername(userName);
        return userMapper.selectOne(user);
    }
}
