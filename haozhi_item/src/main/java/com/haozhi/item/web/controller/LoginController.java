package com.haozhi.item.web.controller;

import com.haozhi.item.config.OpenIdProperties;
import com.haozhi.item.pojo.Banner;
import com.haozhi.item.pojo.HzZc;
import com.haozhi.item.pojo.User;
import com.haozhi.item.service.BannerService;
import com.haozhi.item.service.HzZcService;
import com.haozhi.item.service.UserService;
import com.haozhi.item.shiro.WeChatToken;
import com.haozhi.item.web.common.BaseController;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/6 17:18
 */
@Controller
@RequestMapping("login")
public class LoginController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private OpenIdProperties openIdProperties;

    @Autowired
    private BannerService bannerService;
    @Autowired
    private HzZcService hzZcService;

    /*@RequestMapping
    public String login() {
        return "login";
    }*/

    /**
     * 授权登录
     *
     * @param code
     * @return
     */
    @RequestMapping(value = "getWechatGZAccessToken")
    public String loginAutho(String code, Map<String, Object> map, String superId) {
/*        Subject currentUserSubject = SecurityUtils.getSubject();
        //如果当前已经是登录状态，不进行用户名密码验证，直接返回已登录用户信息
        if (currentUserSubject.getPrincipal() != null) {
            *//*User currentUser = (User) session.getAttribute("loginUser");*//*
            return "index/index";
        }*/
        String openId = "";
        User openidByCode = null;
        try {
            //网页授权获取的code调用微信接口拿到Openid
            openidByCode = userService.getOpenidByCode(code);
            openId = openidByCode.getOpenid();
        } catch (Exception e) {
            return "login";
        }

/*            currentUserSubject.login(new WeChatToken(openId));
            User sysUser = (User) currentUserSubject.getPrincipal();*/
        User sysUser = userService.selectByOpenid(openId);
        if (sysUser != null) {
            session.setAttribute("loginUser", sysUser);
            List<Banner> bannerList = bannerService.findBannerList();
            List<HzZc> zcList = hzZcService.findZcList();
            map.put("labelList", bannerList);
            map.put("zcList", zcList);
            return "index/index";
        } else {
            map.put("wxImage", openidByCode.getWxImage());
            map.put("wxName", openidByCode.getWxName());
            map.put("superId", superId);
            session.setAttribute("openId", openidByCode.getOpenid());
            return "login";
        }
    }

    @RequestMapping
    public String loginInit(String superId) throws UnsupportedEncodingException {
        //回调地址,要跟下面的地址能调通(getWechatGZAccessToken)
        String backUrl = "http://haozhiqiye.haozhizixun.com/api/login/getWechatGZAccessToken?superId=" + superId;
        /**
         *这儿一定要注意！！首尾不能有多的空格（因为直接复制往往会多出空格），其次就是参数的顺序不能变动
         **/

        //AuthUtil.APPID微信公众号的appId
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + openIdProperties.getAPPID() +
                "&redirect_uri=" + URLEncoder.encode(backUrl, "UTF-8") +
                "&response_type=code" +
                "&scope=snsapi_userinfo" +
                "&state=STATE#wechat_redirect";
        return "redirect:" + url;
    }
}
