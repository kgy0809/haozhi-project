package com.haozhi.item.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author kgy
 * @version 1.0
 * @date 2019/12/27 15:22
 */
public class WeChatToken implements AuthenticationToken {

    private String openId;

    /**
     *
     */
    private static final long serialVersionUID = 4812793519945855483L;

    @Override
    public Object getPrincipal() {
        return openId;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    public WeChatToken(String openId){
        this.openId=openId;
    }
}
