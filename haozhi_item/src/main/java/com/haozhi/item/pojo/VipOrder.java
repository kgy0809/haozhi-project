package com.haozhi.item.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
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
}
