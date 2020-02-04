package com.haozhi.item.dto;

import com.haozhi.item.pojo.Menu;
import lombok.Data;

import java.text.DecimalFormat;
import java.util.List;

/**
 * @author kgy
 * @version 1.0
 * @date 2019/12/30 15:17
 */
@Data
public class MenuDto {
    private Menu menuTwo;
    private List<Menu> menuThree;
    private Integer zjPrice;
    private String doubleZjPrice;

    public void setZjPrice(Integer zjPrice) {
        DecimalFormat df=new DecimalFormat("0.00");
        this.doubleZjPrice = df.format(zjPrice/100);
        this.zjPrice = zjPrice;
    }
}
