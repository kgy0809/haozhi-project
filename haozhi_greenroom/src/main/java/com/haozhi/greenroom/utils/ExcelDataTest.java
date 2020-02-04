package com.haozhi.greenroom.utils;

import lombok.Data;

@Data
public class ExcelDataTest {
	/**
	 * 序号
	 */
	private String id;
    /**
     * 会员id
     */
    private String hyId;
    /**
     * 会员注册时间
     */
    private String zcTime;
    /**
     * 会员到期时间
     */
    private String hyTime;
    /**
     * 会员姓名
     */
    private String hyName;
    /**
     * 会员电话
     */
    private String hyTel;
    /**
     * 会员银行卡
     */
    private String hyBankId;
    /**
     * 一级会员数量
     */
    private String oneHyNum;
    /**
     * 二级会员数量
     */
    private String twoHyNum;
    /**
     * 合同编码
     */
    private String htCoding;
    /**
     * 合同类别
     */
    private String aahtClasses;
    /**
     * 业务名称
     */
    private String aaywName;
    /**
     * 数量
     */
    private String num;
    /**
     * 签约日
     */
    private String qySum;
    /**
     * 合同代理费
     */
    private String htDlPrice;
    /**
     * 官费
     */
    private String GfPrice;
    /**
     * 合同总额
     */
    private String zPrice;
    /**
     * vip会员价
     */
    private String vipPrice;
    /**
     * 付款日
     */
    private String fkTime;
    /**
     * 是否已开票
     */
    private String invoice;
    /**
     * 开票日
     */
    private String invoiceTime;
    /**
     * 顾问佣金
     */
    private String gwCommission;
    /**
     * 一级提成
     */
    private String oneTc;
    /**
     * 二级提成
     */
    private String twoTc;
    /**
     * 合同甲方名称
     */
    private String htOwnerName;
    /**
     * 甲方联系人
     */
    private String htOwenr;
    /**
     * 甲方电话
     */
    private String htOwenrTel;
    /**
     * 甲方邮箱
     */
    private String owenrEmail;
    /**
     * 备注
     */
    private String remark;
}
