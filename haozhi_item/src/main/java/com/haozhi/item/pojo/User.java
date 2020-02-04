package com.haozhi.item.pojo;

import lombok.Data;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * @author kgy
 * @version 1.0
 * @date 2019/12/27 14:07
 */

@Data
@Table(name = "haozhi_user")
public class User {

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
     * vip到期 订单id
     */
    private String vipTimeId;

    @Transient
    private String doubleBalance;

    public void setBalance(Integer balance) {
        this.doubleBalance = balance / 100 + "." + balance % 100 / 10 + balance % 100 % 10;
        this.balance = balance;
    }
}
