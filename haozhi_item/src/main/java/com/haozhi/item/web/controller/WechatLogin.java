/*
package com.haozhi.item.web.controller;

import com.haozhi.common.enums.ExceptionEnum;
import com.haozhi.common.exception.LyException;
import com.haozhi.item.pojo.User;
import com.haozhi.item.service.UserService;
import com.haozhi.item.shiro.WeChatToken;
import com.haozhi.item.web.common.BaseController;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

*/
/**
 * @author kgy
 * @version 1.0
 * @date 2019/12/27 15:14
 *//*

@Controller
public class WechatLogin extends BaseController {

    @Autowired
    private UserService userService;


    @RequestMapping("/authorization")
    public String  wechatLogin(String code) {
        Subject currentUserSubject = SecurityUtils.getSubject();
        //如果当前已经是登录状态，不进行用户名密码验证，直接返回已登录用户信息
        if (currentUserSubject.getPrincipal() != null) {
            User currentUser = (User) session.getAttribute("user");
            return "redirect:index/index";
        }
        String openId = "";
        try {
            //网页授权获取的code调用微信接口拿到Openid
            openId = userService.getOpenidByCode(code);
        } catch (Exception e) {
            throw new LyException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        try {
            currentUserSubject.login(new WeChatToken(openId));
        } catch (Exception e) {
            throw new LyException(ExceptionEnum.WX_ERROR);
        }
        User sysUser = (User) currentUserSubject.getPrincipal();
        session.setAttribute("user", sysUser);
        return "redirect:index/index";
    }
}
*/
