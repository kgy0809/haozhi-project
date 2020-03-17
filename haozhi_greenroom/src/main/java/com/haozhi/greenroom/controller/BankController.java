package com.haozhi.greenroom.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.haozhi.common.dto.PageResultDTO;
import com.haozhi.greenroom.dao.BankMapper;
import com.haozhi.greenroom.dao.HzUserMapper;
import com.haozhi.greenroom.pojo.Bank;
import com.haozhi.greenroom.pojo.HzUser;
import com.haozhi.greenroom.pojo.SystemUser;
import com.haozhi.greenroom.pojo.VipTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

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

    @GetMapping
    public String queryList(){
        return "tgls/system/bank/bank_list";
    }

    @PostMapping
    @ResponseBody
    public PageResultDTO queryUser(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                   @RequestParam(value = "rows", defaultValue = "10") Integer rows) {
        PageHelper.startPage(page,rows);
        List<Bank> banks = bankMapper.selectAll();
        for (Bank bank : banks) {
            HzUser user = hzUserMapper.selectByPrimaryKey(bank.getUserId());
            bank.setUserId(user.getName());
        }
        PageInfo<Bank> bankPageInfo = new PageInfo<>(banks);
        return new PageResultDTO(bankPageInfo.getTotal(), bankPageInfo.getList());
    }

}
