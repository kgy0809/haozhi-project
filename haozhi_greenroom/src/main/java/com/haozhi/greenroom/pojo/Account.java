package com.haozhi.greenroom.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.text.SimpleDateFormat;
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
    @Transient
    private String uId;
    @Transient
    private String bankId;
    @Transient
    private String bankName;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM")
    private Date time;
    @Transient
    private String sTime;


    public void setTime(Date time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.sTime = simpleDateFormat.format(time);
        this.time = time;
    }
    public void setPrice(Integer price) {
        this.doublePrice = price / 100 + "." + price % 100 / 10 + price % 100 % 10;
        this.price = price;
    }
}
