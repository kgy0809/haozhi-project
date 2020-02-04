package com.haozhi.item.service;

import com.haozhi.common.utils.SendSMS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/11 11:22
 */
@Service
public class SmsService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 发送短信验证码
     *
     * @param tel
     */
    public void sendCode(String tel) {
        String code = new SendSMS(tel).getSend();
        //保存redis,采用value ,参数1 value值 参数2 是存活时间  参数3 是时间单位
        redisTemplate.boundValueOps(tel).set(code, 5l, TimeUnit.MINUTES);
    }

}
