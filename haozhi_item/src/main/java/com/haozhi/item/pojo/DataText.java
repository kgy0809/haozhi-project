package com.haozhi.item.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author kgy
 * @version 1.0
 * @date 2019/12/26 11:11
 */
@Data
@Table(name = "haozhi_data")
public class DataText {
    /**
     * 资料ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    /**
     * 资料head
     */
    private String title;
    /**
     * 资料展示图
     */
    private String image;
    /**
     * 资料详情
     */
    private String content;
    /**
     * 资料发表时间
     */
    private Date time;

    @Transient
    private String sTime;

    public void setsTime(String sTime) {
        Date time = this.time;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.sTime = simpleDateFormat.format(time);
    }

}
