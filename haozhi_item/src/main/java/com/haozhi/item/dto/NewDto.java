package com.haozhi.item.dto;

import lombok.Data;

import java.text.DecimalFormat;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/4 14:17
 */
@Data
public class NewDto {
    private Integer newNum;
    private Integer newPrice;
    private String doubleNewPrice;

    public void setNewPrice(Integer newPrice) {
        DecimalFormat df=new DecimalFormat("0.00");
        this.doubleNewPrice = df.format(newPrice/100);
        this.newPrice = newPrice;
    }
}
