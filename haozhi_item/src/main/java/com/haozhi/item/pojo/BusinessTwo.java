package com.haozhi.item.pojo;

import lombok.Data;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author kgy
 * @version 1.0
 * @date 2019/12/28 10:29
 */
@Data
@Table(name = "haozhi_businesstwo")
public class BusinessTwo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    /**
     * 商标名称
     */
    private String sbName;
    /**
     * 商标类型 1文字2图形
     */
    private String sbType;
    /**
     * 商标颜色 0不指定 1指定
     */
    private String sbColor;
    /**
     * 商标样式 文字为空 图片为url保存
     */
    private String sbImage;
    /**
     * 上一页携带过来的id
     */
    private String oneId;
    /**
     * menu数据
     */
    private String menuId;
    /**
     * 价格金额
     */
    private Integer price;
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
     * 附加代理费
     */
    private Integer commission;
    /**
     * 订单数量
     */
    private Integer number;
    /**
     * 类别
     */
    private String lbVal;
    /**
     * 注册号
     */
    private String zchVal;
    private Date time;
    @Transient
    private String sTime;
    @Transient
    private String doubleCommission;
    @Transient
    private String doublePrice;
    @Transient
    private String integerPrice;

    public void setPrice(Integer price) {
        this.doublePrice = price / 100 + "." + price % 100 / 10 + price % 100 % 10;
        this.integerPrice = String.valueOf(price / 100);
        this.price = price;
    }



    public void setCommission(Integer commission) {
        this.doubleCommission = commission / 100 + "." + commission % 100 / 10 + commission % 100 % 10;
        this.commission = commission;
    }

    public void setTime(Date time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.sTime = simpleDateFormat.format(time);
        this.time = time;
    }
}
