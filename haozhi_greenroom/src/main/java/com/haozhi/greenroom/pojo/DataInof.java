package com.haozhi.greenroom.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/12 12:30
 */
@Data
@Table(name = "haozhi_data")
public class DataInof {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String title;
    private String image;
    @Transient
    private String file;
    private String content;
    private Date time;
    @Transient
    private String sTime;

    public void setTime(Date time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.sTime = simpleDateFormat.format(time);
        this.time = time;
    }


}
