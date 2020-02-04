package com.haozhi.item.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/6 9:11
 */
@Data
@Table(name = "haozhi_regard")
public class Regard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String regard;
}
