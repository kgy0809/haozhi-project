package com.haozhi.greenroom.pojo;

import lombok.Data;

import javax.persistence.Transient;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/3 10:55
 */
@Data
public class LastDto {

    private HzYw hzYw;

    private String id;
    /**
     * 申请人 信息
     */
    private String sqrMessage;
    private String imgtext;
    /**
     * 商标名称
     */
    private String sbName;
    /**
     * 商标状态 1 文字  2 图形
     */
    private String sbType;
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
    private List<HzMenu> menuName;
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

    /**
     * 商标颜色 0不指定 1指定
     */
    private String sbColor;
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
     * 附加代理费
     */
    private Integer commission;
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
    /**
     * 订单数量
     */
    private Integer number;
    @Transient
    private String doubleCommission;
    @Transient
    private String integerPrice;

    public void setCommission(Integer commission) {
        this.doubleCommission = commission / 100 + "." + commission % 100 / 10 + commission % 100 % 10;
        this.commission = commission;
    }
    public void setPrice(Integer price) {
        if (price!=null){
            DecimalFormat df=new DecimalFormat("0.00");
            this.doublePrice = df.format(price/100);
        }
        Price = price;
    }

    public void setTime(Date time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.sTime = simpleDateFormat.format(time);
        this.time = time;
    }
}
