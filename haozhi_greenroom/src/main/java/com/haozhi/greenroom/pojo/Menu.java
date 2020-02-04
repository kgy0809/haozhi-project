package com.haozhi.greenroom.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/12 10:10
 */
@Data
@Table(name = "system_menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String url;
    private Integer pid;
}
