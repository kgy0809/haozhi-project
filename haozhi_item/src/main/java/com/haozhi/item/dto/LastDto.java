package com.haozhi.item.dto;

import com.haozhi.item.pojo.Menu;
import lombok.Data;

import javax.persistence.Transient;
import java.text.DecimalFormat;
import java.util.List;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/3 10:55
 */
@Data
public class LastDto {

    private String id;
    /**
     * 申请人 信息
     */
    private String sqrMessage;
    /**
     * 商标名称
     */
    private String sbName;
    /**
     * 商标状态 1 文字  2 图形
     */
    private String sbType;
    private String imgText;
    /**
     * 文字为空 图片为 url
     */
    private String sbImage;
    /**
     * 一级类别
     */
    private String oneName;
    /**
     * 三级类别
     */
    private List<Menu> menuName;
    /**
     * 1企业申请2个人申请
     */
    private String application;
    /**
     * 公司名称   个人姓名
     */
    private String applicationName;
    /**
     * 统一社会信用代码   身份证号
     */
    private String applicationId;
    /**
     * 联系人名称
     */
    private String applicationNumName;
    /**
     * 联系电话
     */
    private String applicationNumTel;
    /**
     * 电子邮箱
     */
    private String applicationNumMail;
    /**
     * 合计
     */
    private Integer Price;
    @Transient
    private String doublePrice;

    public void setPrice(Integer price) {
        if (price!=null){
            DecimalFormat df=new DecimalFormat("0.00");
            this.doublePrice = df.format(price/100);
        }
        Price = price;
    }
}
