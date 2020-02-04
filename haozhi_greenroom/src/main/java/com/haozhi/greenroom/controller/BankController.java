package com.haozhi.greenroom.controller;

import com.haozhi.greenroom.dao.BankMapper;
import com.haozhi.greenroom.dao.HzUserMapper;
import com.haozhi.greenroom.pojo.Bank;
import com.haozhi.greenroom.pojo.HzUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/17 16:41
 */
@Controller
@RequestMapping("bank")
public class BankController {

    @Autowired
    private BankMapper bankMapper;

    @Autowired
    private HzUserMapper hzUserMapper;

    @RequestMapping
    public String bank(Map<String, Object> map){
        List<Bank> banks = bankMapper.selectAll();
        List<Bank> list = new ArrayList<>();
        for (Bank bank : banks) {
            HzUser user = hzUserMapper.selectByPrimaryKey(bank.getUserId());
            bank.setUserId(user.getName());
            list.add(bank);
        }
        map.put("bank",list);
        return "tgls/system/bank/bank_list";
    }
}
