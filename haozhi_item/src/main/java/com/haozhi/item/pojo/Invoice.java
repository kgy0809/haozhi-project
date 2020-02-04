package com.haozhi.item.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/6 11:42
 */
@Data
@Table(name = "haozhi_invoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    /**
     * 1企业申请2个人
     */
    private String type;
    private String fpName;
    private String duty;
    private String remark;
    private String addrAndTel;
    private String bankAndAddr;
    private String invoiceOrder;
    private Integer invoicePrice;
    @Transient
    private String doubleInvoicePrice;
    private String name;
    private String tel;
    private String address;
    private String express;
    private String userId;
    private String userName;
    /**
     * 1已提交2已审核3已邮寄
     */
    private String state;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date time;

    public void setInvoicePrice(Integer invoicePrice) {
        this.doubleInvoicePrice = invoicePrice / 100 + "." + invoicePrice % 100 / 10 + invoicePrice % 100 % 10;
        this.invoicePrice = invoicePrice;
    }
}
