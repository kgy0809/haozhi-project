package com.haozhi.greenroom.pojo;

import lombok.Data;

import javax.persistence.*;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/13 17:45
 */
@Data
@Table(name = "haozhi_vip")
public class Vip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private Integer price;
    private Integer couponPrice;
    @Transient
    private String dprice;
    @Transient
    private String dcouponPrice;

    private Integer personNum;

    public void setPrice(Integer price) {
        this.dprice = String.valueOf(price / 100);
        this.price = price;
    }

    public void setCouponPrice(Integer couponPrice) {
        this.dcouponPrice = String.valueOf(couponPrice / 100);
        this.couponPrice = couponPrice;
    }
}
