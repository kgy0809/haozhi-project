package com.haozhi.item.pojo;

import lombok.Data;

import javax.persistence.*;

/**
 * @author kgy
 * @version 1.0
 * @date 2019/12/16 10:43
 */
@Data
@Table(name = "haozhi_banner")
public class Banner {

    /**
     * 轮播图ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    /**
     * 轮播图url
     */
    private String image;
}
