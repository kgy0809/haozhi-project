package com.haozhi.item.pojo;

import lombok.Data;

import javax.persistence.*;

/**
 * @author kgy
 * @version 1.0
 * @date 2019/12/27 10:01
 */
@Table(name = "haozhi_zc")
@Data
public class HzZc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String name;
    private String url;
    private String png;
    private String coding;
}
