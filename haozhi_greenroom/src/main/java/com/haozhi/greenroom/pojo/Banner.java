package com.haozhi.greenroom.pojo;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String image;
}
