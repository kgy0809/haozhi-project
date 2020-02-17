package com.haozhi.greenroom.pojo;

import lombok.Data;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/12 15:05
 */
@Data
@Table(name = "haozhi_user")
public class SystemUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String name;
    private String openid;
    private String tel;
    private String mail;
    private String wxName;
    private String wxImage;
    private String state;
    private String superId;
    private Date time;
    @Transient
    private Date timeVip;
    @Transient
    private String sTime;
    private String city;
    @Transient
    private String code;
    /**
     * 优惠券 数量
     */
    private Integer coupon;
    /**
     * 当月单数
     */
    private Integer monthNum;
    /**
     * 累计单数
     */
    private Integer totalNum;
    /**
     * 个人余额
     */
    private Integer balance;
    /**
     * vip id
     */
    private String vipTimeId;

    @Transient
    private String doubleBalance;

    @Transient
    private String vipTime;

    public void setBalance(Integer balance) {
        this.doubleBalance = balance / 100 + "." + balance % 100 / 10 + balance % 100 % 10;
        this.balance = balance;
    }

    public void setTime(Date time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.sTime = simpleDateFormat.format(time);
        this.time = time;
    }

    public void setTimeVip(Date timeVip) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.vipTime = simpleDateFormat.format(timeVip);
        this.timeVip = timeVip;
    }
}
