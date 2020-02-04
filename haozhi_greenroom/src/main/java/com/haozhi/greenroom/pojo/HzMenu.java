package com.haozhi.greenroom.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/15 16:36
 */
@Data
@Table(name = "haozhi_menu")
public class HzMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer pid;
}
