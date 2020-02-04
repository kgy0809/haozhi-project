package com.haozhi.item.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author kgy
 * @version 1.0
 * @date 2019/12/26 11:11
 */
@Data
@Table(name = "haozhi_data")
public class DataTextDto {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String title;
    private String image;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date time;

}
