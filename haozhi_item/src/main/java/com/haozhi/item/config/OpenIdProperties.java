package com.haozhi.item.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author kgy
 * @version 1.0
 * @date 2019/12/20 14:47
 */
@Data
@Component
@ConfigurationProperties("haozhi.openid")
public class OpenIdProperties {

    private String APPID;
    private String SECRET;
}
