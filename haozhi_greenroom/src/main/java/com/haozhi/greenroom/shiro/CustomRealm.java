package com.haozhi.greenroom.shiro;

import com.haozhi.greenroom.pojo.User;
import com.haozhi.greenroom.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义的realm
 * @author kgy
 * @version 1.0
 * @date 2019/12/13 11:19
 */
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    public void setName(String name){
        super.setName("customRealm");
    }

    /**
     * 授权方法
     *      操作的时候：判断用户是否具有相应的权限
     *          先认证 --  安全数据
     *          在授权 --  根据安全数据获取用户具有的所以操作权限
     * @param principalCollection
     * @return
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //1.获取已认证的用户数据
        User user = (User) principalCollection.getPrimaryPrincipal();
        //2.根据用户数据获取用户的权限信息（所有角色，所有权限）
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> stringSet = new HashSet<>();

        stringSet.add("user:show");
        stringSet.add("user:admin");

        info.setStringPermissions(stringSet);
        return info;
    }

    /**
     * 认证方法
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("-------身份认证方法--------");
        String userName = (String) authenticationToken.getPrincipal();
        String userPwd = new String((char[]) authenticationToken.getCredentials());
        //根据用户名从数据库获取密码
        User user = userService.selectUser(userName);
        //判断用户名是否存在或者密码是否正确
        if (userName == null) {
            throw new AccountException("用户名不正确");
        } else if (!userPwd.equals(user.getPassword())) {
            throw new AccountException("密码不正确");
        }
        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配
        //构造方法: 安全数据,密码,realm域名字
        return new SimpleAuthenticationInfo(user, user.getPassword(), this.getName());
    }

}
