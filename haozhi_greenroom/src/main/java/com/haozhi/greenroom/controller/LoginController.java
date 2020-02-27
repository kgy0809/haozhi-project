package com.haozhi.greenroom.controller;

import com.haozhi.greenroom.pojo.Menu;
import com.haozhi.greenroom.service.MenuService;
import com.haozhi.greenroom.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/6 14:01
 */
@Controller
@RequestMapping("login")
public class LoginController {

    @Autowired
    private MenuService menuService;
    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public String login(){
        return "login";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String loginUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            Map<String, Object> map,
            HttpSession session
    ) {
        // 从SecurityUtils里边创建一个 subject
        Subject subject = SecurityUtils.getSubject();
        //对密码进行加密处理 采用 shiro的MD5加密 参数一:加密的参数 参数二:盐 参数三:加密次数
        password = new Md5Hash(password, username, 3).toString();
        // 在认证提交前准备 token（令牌）
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        // 执行认证登陆
        try {
            subject.login(token);
            session.setAttribute("loginUser", username);
            List<Menu> menuOne = menuService.queryMenu();
            map.put("menu",menuOne);
            return "frame";
        } catch (Exception uae) {
            map.put("msg", "用户名或密码错误！");
            return "login";
        }
    }

    @RequestMapping("loginOut")
    public String loginOut(){
        try {
            Subject subject = SecurityUtils.getSubject();
            subject.logout();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/login";
    }

    @GetMapping("password")
    public String password() {
        return "tgls/modify_password";
    }

    /**
     * 修改密码
     * @param oldPassword
     * @param newPassword
     * @param password
     * @param session
     * @return
     */
    @PostMapping("change")
    @ResponseBody
    public Map<String,Object> change(String oldPassword, String newPassword, String password, HttpSession session ) {
        Map<String, Object> map=new HashMap<>();
        map.put("code",200);
        map.put("msg", "修改成功");
        if (!password.equals(newPassword)) {
            map.put("code",500);
            map.put("msg", "两次输入密码不同");
        }
        String loginUser = (String) session.getAttribute("loginUser");
        if (userService.updatePassword(loginUser, oldPassword, password) == 0){
            map.put("code",500);
            map.put("msg", "原始密码错误");
        }
        return map;
    }
}
