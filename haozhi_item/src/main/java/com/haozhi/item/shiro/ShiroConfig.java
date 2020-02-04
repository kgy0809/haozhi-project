/*
package com.haozhi.item.shiro;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

*/
/**
 * @author kgy
 * @version 1.0
 * @date 2019/12/13 11:17
 *//*


@Configuration
public class ShiroConfig {

    */
/**
     * 创建realm
     *
     * @return
     *//*


    @Bean
    public WechatRealm getRemale() {
        return new WechatRealm();
    }

    */
/**
     * 创建安全管理器
     *
     * @return
     *//*


    @Bean
    public DefaultWebSecurityManager getSecurityManager(WechatRealm realm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);
        return securityManager;
    }

    */
/**
     * 配置shiro的过滤器工厂
     * 在web程序中，shiro进行权限控制全部是通过一组过滤器集合进行配置控制
     *
     * @param securityManager
     * @return
     *//*


    @Bean
    public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager) {
        //1.创建过滤器工厂
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //2.设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //3.通用配置（跳转登陆页面，为授权跳转的页面）
        shiroFilterFactoryBean.setLoginUrl("/index/index");
        shiroFilterFactoryBean.setUnauthorizedUrl("/login");
        //注销跳转页面
        LogoutFilter logoutFilter = new LogoutFilter();
        logoutFilter.setRedirectUrl("/login");
        //4.设置过滤器集合
*/
/**
 * 设置所有的过滤器，有顺序map
 *      key = 拦截的url地址
 *      value = 过滤器类型
 * *//*


        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        filterChainDefinitionMap.put("/login/**", "anon");
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/image/**", "anon");//images
        filterChainDefinitionMap.put("/css/**", "anon");//css
        filterChainDefinitionMap.put("/js/**", "anon");//js
        filterChainDefinitionMap.put("/sms/**", "anon");//发送短信
        filterChainDefinitionMap.put("/user/**", "anon");//用户注册
        //主要这行代码必须放在所有权限设置的最后，不然会导致所有 url 都被拦截 剩余的都需要认证
        filterChainDefinitionMap.put("/**", "anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

}
*/
