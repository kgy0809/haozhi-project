package com.haozhi.item.web.controller;

import com.haozhi.common.constants.StatusCode;
import com.haozhi.common.dto.ResultDTO;
import com.haozhi.item.dao.RegisterAgreementMapper;
import com.haozhi.item.pojo.RegisterAgreement;
import com.haozhi.item.pojo.User;
import com.haozhi.item.service.UserService;
import com.haozhi.item.web.common.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/11 11:10
 */
@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private RegisterAgreementMapper registerAgreementMapper;

    /**
     * 用户注册
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO register(User user) {
        user.setOpenid(String.valueOf(session.getAttribute("openId")));
        List<User> tel = userService.querytel(user.getTel());
        if (tel != null && tel.size() > 0)
            return new ResultDTO(true, StatusCode.LOGINERROR, "手机号已经存在");
        if (!userService.querySms(user.getCode(), user.getTel()))
            return new ResultDTO(true, StatusCode.ACCESSERROR, "验证码错误");
        userService.saveUser(user);
        return new ResultDTO(true, StatusCode.OK, "注册成功,信息存入成功");
    }

    /**
     * 用户注册查看协议
     */
    @RequestMapping(value = "agreement")
    public String agreement(Map<Object, Object> map) {
        List<RegisterAgreement> registerAgreements = registerAgreementMapper.selectAll();
        map.put("list",registerAgreements.get(0));
        return "register_agreement";
    }
}
