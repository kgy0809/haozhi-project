/*
package com.haozhi.item.shiro;

import com.haozhi.item.pojo.User;
import com.haozhi.item.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

*/
/**
 * 自定义的realm
 *
 * @author kgy
 * @version 1.0
 * @date 2019/12/13 11:19*//*



public class WechatRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    public void setName(String name) {
        super.setName("WechatRealm");
    }

*/
/**
     * 授权方法
     * 操作的时候：判断用户是否具有相应的权限
     * 先认证 --  安全数据
     * 在授权 --  根据安全数据获取用户具有的所以操作权限
     *
     * @param principalCollection
     * @return*//*




    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        try {
            //Role和Permission逻辑按需添加
            SimpleAuthorizationInfo simpleAuthorInfo = new SimpleAuthorizationInfo();
            simpleAuthorInfo.addStringPermission("all");
            return simpleAuthorInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        WeChatToken token = (WeChatToken) authcToken;

        User user = userService.selectByOpenid((String) token.getPrincipal());
        if (user == null) {
            throw new AuthenticationException("用户不存在");
        }
        AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, null, this.getName());
        return authenticationInfo;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token != null && token instanceof WeChatToken;
    }

    */
/**
     * 认证密码匹配调用方法
     *//*

    @Override
    protected void assertCredentialsMatch(AuthenticationToken authcToken,
                                          AuthenticationInfo info) throws AuthenticationException {
        return;
    }

}
*/
