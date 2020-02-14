package com.haozhi.item.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/4 9:30
 */
@Data
@Table(name = "haozhi_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String title;
    private String sbName;
    @Transient
    private String doubleFwPrice;
    private Integer fwPrice;
    @Transient
    private String doubleGfPrice;
    private Integer gfPrice;
    private Date time;
    @Transient
    private String sTime;
    private Integer price;
    @Transient
    private String doublePrice;
    private Integer state;
    private String pOrder;
    private String userId;
    private String invoiceState;
    private String remark;
    private Integer number;
    private String openid;
    private String power;//委托书
    private String contract;//合同
    private Date invoiceTime;//开票日期
    private String xxPayStart;//是否线下支付
    @Transient
    private String vipState;//购买vip约定

    public void setPrice(Integer price) {
        this.doublePrice = price / 100 + "." + price % 100 / 10 + price % 100 % 10;
        this.price = price;
    }

    public void setFwPrice(Integer fwPrice) {
        this.doubleFwPrice = fwPrice / 100 + "." + fwPrice % 100 / 10 + fwPrice % 100 % 10;
        this.fwPrice = fwPrice;
    }

    public void setGfPrice(Integer gfPrice) {
        this.doubleGfPrice = gfPrice / 100 + "." + gfPrice % 100 / 10 + gfPrice % 100 % 10;
        this.gfPrice = gfPrice;
    }


    public void setTime(Date time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.sTime = simpleDateFormat.format(time);
        this.time = time;
    }
}
