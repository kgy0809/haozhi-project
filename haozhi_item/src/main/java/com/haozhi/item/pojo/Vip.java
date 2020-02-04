package com.haozhi.item.pojo;

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
    private Integer personNum;

}
