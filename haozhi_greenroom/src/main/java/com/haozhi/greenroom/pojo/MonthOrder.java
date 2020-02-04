package com.haozhi.greenroom.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/8 13:26
 */
@Data
@Table(name = "haozhi_month_order")
public class MonthOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private Date time;
    private String userId;
    private String state;
    private String orderId;
    private Integer price;
    @Transient
    private String doublePrice;

    public void setPrice(Integer price) {
        this.doublePrice = price / 100 + "." + price % 100 / 10 + price % 100 % 10;
        this.price = price;
    }
}
