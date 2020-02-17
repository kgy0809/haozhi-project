package com.haozhi.greenroom.pojo;

import lombok.Data;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Table(name = "haozhi_vip_order")
public class VipOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private Integer price;
    private String openid;
    private String userId;
    private Date time;
    private Integer count;
    private String state;
    @Transient
    private String sTime;
    @Transient
    private String userName;


    public void setTime(Date time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.sTime = simpleDateFormat.format(time);
        this.time = time;
    }
}
