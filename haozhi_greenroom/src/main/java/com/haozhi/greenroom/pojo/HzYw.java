package com.haozhi.greenroom.pojo;

import lombok.Data;

import javax.persistence.*;

/**
 * @author kgy
 * @version 1.0
 * @date 2019/12/27 10:19
 */
@Data
@Table(name = "haozhi_yw")
public class HzYw {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String name;
    private Integer vipPrice;
    private Integer hyPrice;
    private String text;
    private String image;
    private String zid;
    private String title;
    private String bjPrice;
    private String dsPrice;
    private Integer gfPrice;
    private String sjName;
    private String wordUrl;
    @Transient
    private String doubleVipPrice;
    @Transient
    private String doubleHyPrice;
    @Transient
    private String doubleGfPrice;

    public void setVipPrice(Integer vipPrice) {
        this.doubleVipPrice = vipPrice / 100 + "." + vipPrice % 100 / 10 + vipPrice % 100 % 10;
        this.vipPrice = vipPrice;
    }

    public void setHyPrice(Integer hyPrice) {
        this.doubleHyPrice = hyPrice / 100 + "." + hyPrice % 100 / 10 + hyPrice % 100 % 10;
        this.hyPrice = hyPrice;
    }

    public void setGfPrice(Integer gfPrice) {
        this.doubleGfPrice = gfPrice / 100 + "." + gfPrice % 100 / 10 + gfPrice % 100 % 10;
        this.gfPrice = gfPrice;
    }
}
