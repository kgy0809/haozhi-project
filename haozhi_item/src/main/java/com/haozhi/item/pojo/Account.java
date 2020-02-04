package com.haozhi.item.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/8 13:42
 */
@Data
@Table(name = "haozhi_account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private Integer price;
    @Transient
    private String doublePrice;
    private String state;
    private String userId;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM")
    private Date time;

    public void setPrice(Integer price) {
        this.doublePrice = price / 100 + "." + price % 100 / 10 + price % 100 % 10;
        this.price = price;
    }
}
