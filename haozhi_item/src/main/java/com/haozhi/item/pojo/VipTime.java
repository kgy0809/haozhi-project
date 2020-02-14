package com.haozhi.item.pojo;

import lombok.Data;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/14 9:07
 */
@Data
@Table(name = "haozhi_vip_time")
public class VipTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 创建时间
     */
    private Date joinTime;
    /**
     * 到期时间
     */
    private Date expireTime;
    @Transient
    private String sexpireTime;

    /**
     * vip id
     */
    private String vipId;
    private String state;

    public void setExpireTime(Date time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.sexpireTime = simpleDateFormat.format(time);
        this.expireTime = time;
    }
}
