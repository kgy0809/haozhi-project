package com.haozhi.item.web.common;

import com.haozhi.item.pojo.User;
import com.haozhi.item.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BaseController {

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    @Autowired
    protected HttpSession session;

    @Autowired
    private UserService userService;

    /**
     * 获取当前登录用户
     */
    protected User getUser() {
        Object object = session.getAttribute("loginUser");
        User user = userService.queryById("1227493892644016128");
        if (object != null) {
            return (User) object;
        }
        return null;
    }
}
