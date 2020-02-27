package com.haozhi.greenroom.service;

import com.haozhi.greenroom.dao.UserMapper;
import com.haozhi.greenroom.pojo.User;
import org.apache.shiro.crypto.hash.Md5Hash;
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
    public int updatePassword(String loginUser, String oldPassword,String password) {
        User user = new User();
        user.setUsername(loginUser);
        User selectOne = userMapper.selectOne(user);
        String onePassword = selectOne.getPassword();
        if (onePassword.equals(new Md5Hash(oldPassword,loginUser,3).toString())){
            User selectUser = selectUser(loginUser);
            selectUser.setPassword(new Md5Hash(password,loginUser,3).toString());
            return userMapper.updateByPrimaryKey(selectUser);
        }
        return 0;
    }
}
