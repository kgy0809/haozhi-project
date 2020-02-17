package com.haozhi.greenroom.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.haozhi.common.dto.PageResultDTO;
import com.haozhi.greenroom.dao.AccountMapper;
import com.haozhi.greenroom.dao.BankMapper;
import com.haozhi.greenroom.dao.HzUserMapper;
import com.haozhi.greenroom.pojo.Account;
import com.haozhi.greenroom.pojo.Bank;
import com.haozhi.greenroom.pojo.HzUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/17 18:40
 */
@Controller
@RequestMapping("deposit")
public class AccountController {

    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private HzUserMapper hzUserMapper;
    @Autowired
    private BankMapper bankMapper;

    @RequestMapping(method = RequestMethod.GET)
    public String deposit() {
        return "tgls/system/deposit/deposit_list";
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public PageResultDTO pageDeposit(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                     @RequestParam(value = "rows", defaultValue = "10") Integer rows,
                                     @RequestParam(value = "uid", required = false) String uid,
                                     @RequestParam(value = "id", required = false) String id,
                                     @RequestParam(value = "time1", required = false) String time1,
                                     @RequestParam(value = "time2", required = false) String time2
    ) {
        if (uid != null && !("").equals(uid)) {
            Example example = new Example(Account.class);
            example.createCriteria().andEqualTo("userId", uid);
            List<Account> accounts = accountMapper.selectByExample(example);
            List<Account> list = new ArrayList<>();
            if (accounts.size() != 0) {
                for (Account account : accounts) {
                    HzUser user = hzUserMapper.selectByPrimaryKey(account.getUserId());
                    account.setUserId(user.getName());
                    account.setUId(user.getId());
                    Example example1 = new Example(Bank.class);
                    example1.createCriteria().andEqualTo("userId", user.getId());
                    Bank bank = bankMapper.selectOneByExample(example1);
                    account.setBankId(bank.getBankId());
                    account.setBankName(bank.getBankName());
                    list.add(account);
                }
            }
            return new PageResultDTO((long) list.size(), list);
        }
        if (id != null && !("").equals(id)) {
            Account account = accountMapper.selectByPrimaryKey(id);
            List<Account> list = new ArrayList<>();
            if (account != null) {
                HzUser user = hzUserMapper.selectByPrimaryKey(account.getUserId());
                account.setUserId(user.getName());
                account.setUId(user.getId());
                Example example1 = new Example(Bank.class);
                example1.createCriteria().andEqualTo("userId", user.getId());
                Bank bank = bankMapper.selectOneByExample(example1);
                account.setBankId(bank.getBankId());
                account.setBankName(bank.getBankName());
                list.add(account);
            }
            return new PageResultDTO((long) list.size(), list);
        }

        if ((time1 != null && !("").equals(time1)) || (time2 != null && !("").equals(time2))) {
            List<Account> accounts = accountMapper.selectByTime1AndTime2(time1, time2);
            List<Account> list = new ArrayList<>();
            if (accounts.size() != 0) {
                for (Account account : accounts) {
                    HzUser user = hzUserMapper.selectByPrimaryKey(account.getUserId());
                    account.setUserId(user.getName());
                    account.setUId(user.getId());
                    Example example1 = new Example(Bank.class);
                    example1.createCriteria().andEqualTo("userId", user.getId());
                    Bank bank = bankMapper.selectOneByExample(example1);
                    account.setBankId(bank.getBankId());
                    account.setBankName(bank.getBankName());
                    list.add(account);
                }
            }
            return new PageResultDTO((long) list.size(), list);
        }
        Example example = new Example(Account.class);
        example.setOrderByClause("time desc");
        example.createCriteria().andNotEqualTo("state", "1");
        PageHelper.startPage(page, rows);
        List<Account> accounts = accountMapper.selectByExample(example);
        List<Account> list = new ArrayList<>();
        for (Account account : accounts) {
            HzUser user = hzUserMapper.selectByPrimaryKey(account.getUserId());
            account.setUserId(user.getName());
            account.setUId(user.getId());
            Example example1 = new Example(Bank.class);
            example1.createCriteria().andEqualTo("userId", user.getId());
            Bank bank = bankMapper.selectOneByExample(example1);
            account.setBankId(bank.getBankId());
            account.setBankName(bank.getBankName());
            list.add(account);
        }
        PageInfo<Account> accountPageInfo = new PageInfo<>(list);
        return new PageResultDTO(accountPageInfo.getTotal(), accountPageInfo.getList());
    }

    @RequestMapping("update")
    public String update(String id) {
        Account account = new Account();
        account.setId(id);
        account.setState("3");
        accountMapper.updateByPrimaryKeySelective(account);
        return "redirect:/deposit";
    }
}
