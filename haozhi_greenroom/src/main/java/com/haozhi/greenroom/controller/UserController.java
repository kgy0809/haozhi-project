package com.haozhi.greenroom.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.haozhi.common.dto.PageResultDTO;
import com.haozhi.greenroom.dao.MonthOrderMapper;
import com.haozhi.greenroom.dao.SystemUserMapper;
import com.haozhi.greenroom.dao.VipTimeMapper;
import com.haozhi.greenroom.pojo.MonthOrder;
import com.haozhi.greenroom.pojo.SystemUser;
import com.haozhi.greenroom.pojo.VipTime;
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
 * @date 2020/1/12 15:04
 */
@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private SystemUserMapper systemUserMapper;
    @Autowired
    private VipTimeMapper vipTimeMapper;
    @Autowired
    private MonthOrderMapper monthOrderMapper;

    @RequestMapping(method = RequestMethod.GET)
    public String queryUser() {
        return "tgls/system/user/use_list";
    }

    @RequestMapping(value = "view", method = RequestMethod.GET)
    public String queryUserView() {
        return "tgls/system/user/use_view_list";
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public PageResultDTO queryUser(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                   @RequestParam(value = "rows", defaultValue = "10") Integer rows,
                                   @RequestParam(value = "id", required = false) String id,
                                   @RequestParam(value = "name", required = false) String name,
                                   @RequestParam(value = "phone", required = false) String phone) {
        if (id != null && !("").equals(id)) {
            SystemUser systemUser = systemUserMapper.selectByPrimaryKey(id);
            VipTime vipTime = vipTimeMapper.selectByPrimaryKey(systemUser.getVipTimeId());
            if (vipTime != null) {
                systemUser.setTimeVip(vipTime.getExpireTime());
            }
            List<SystemUser> list = new ArrayList<>();
            list.add(systemUser);
            return new PageResultDTO((long) list.size(), list);
        }
        if (name != null && !("").equals(name)) {
            Example example = new Example(SystemUser.class);
            example.createCriteria().andEqualTo("name", name);
            List<SystemUser> systemUsers = systemUserMapper.selectByExample(example);
            if (systemUsers.size() != 0) {
                for (SystemUser systemUser : systemUsers) {
                    VipTime vipTime = vipTimeMapper.selectByPrimaryKey(systemUser.getVipTimeId());
                    if (vipTime != null) {
                        systemUser.setTimeVip(vipTime.getExpireTime());
                    }
                }
            }
            return new PageResultDTO((long) systemUsers.size(), systemUsers);
        }
        if (phone != null && !("").equals(phone)) {
            Example example = new Example(SystemUser.class);
            example.createCriteria().andEqualTo("tel", phone);
            List<SystemUser> systemUsers = systemUserMapper.selectByExample(example);
            if (systemUsers.size() != 0) {
                for (SystemUser systemUser : systemUsers) {
                    VipTime vipTime = vipTimeMapper.selectByPrimaryKey(systemUser.getVipTimeId());
                    if (vipTime != null) {
                        systemUser.setTimeVip(vipTime.getExpireTime());
                    }
                }
            }
            return new PageResultDTO((long) systemUsers.size(), systemUsers);
        }
        PageHelper.startPage(page, rows);
        Example example = new Example(SystemUser.class);
        example.setOrderByClause("time desc");
        List<SystemUser> systemUsers = systemUserMapper.selectByExample(example);
        for (SystemUser systemUser : systemUsers) {
            VipTime vipTime = vipTimeMapper.selectByPrimaryKey(systemUser.getVipTimeId());
            if (vipTime != null) {
                systemUser.setTimeVip(vipTime.getExpireTime());
            }
        }
        PageInfo<SystemUser> systemUserPageInfo = new PageInfo<>(systemUsers);
        return new PageResultDTO(systemUserPageInfo.getTotal(), systemUserPageInfo.getList());
    }

    @RequestMapping(value = "view", method = RequestMethod.POST)
    @ResponseBody
    public PageResultDTO queryUserView(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                       @RequestParam(value = "rows", defaultValue = "10") Integer rows,
                                       @RequestParam(value = "uid", required = false) String uid,
                                       @RequestParam(value = "time1", required = false) String time1,
                                       @RequestParam(value = "time2", required = false) String time2,
                                       @RequestParam(value = "orderId", required = false) String orderId) {
        if (uid != null && !("").equals(uid)) {
            PageHelper.startPage(page, rows);
            Example example = new Example(MonthOrder.class);
            example.setOrderByClause(" time desc ");
            example.createCriteria().andEqualTo("userId", uid);
            List<MonthOrder> monthOrders = monthOrderMapper.selectByExample(example);
            if (monthOrders.size() != 0) {
                for (MonthOrder monthOrder : monthOrders) {
                    SystemUser systemUser = systemUserMapper.selectByPrimaryKey(monthOrder.getUserId());
                    monthOrder.setUserName(systemUser.getName());
                }
            }
            PageInfo<MonthOrder> systemUserPageInfo = new PageInfo<>(monthOrders);
            return new PageResultDTO(systemUserPageInfo.getTotal(), systemUserPageInfo.getList());
        }
        if (orderId != null && !("").equals(orderId)) {
            PageHelper.startPage(page, rows);
            Example example = new Example(MonthOrder.class);
            example.setOrderByClause(" time desc ");
            example.createCriteria().andEqualTo("orderId", orderId);
            List<MonthOrder> monthOrders = monthOrderMapper.selectByExample(example);
            if (monthOrders.size() != 0) {
                for (MonthOrder monthOrder : monthOrders) {
                    SystemUser systemUser = systemUserMapper.selectByPrimaryKey(monthOrder.getUserId());
                    monthOrder.setUserName(systemUser.getName());
                }
            }
            PageInfo<MonthOrder> systemUserPageInfo = new PageInfo<>(monthOrders);
            return new PageResultDTO(systemUserPageInfo.getTotal(), systemUserPageInfo.getList());
        }
        if ((time1 != null && !("").equals(time1)) || (time2 != null && !("").equals(time2))) {
            PageHelper.startPage(page, rows);
            List<MonthOrder> monthOrders = monthOrderMapper.selectByTimeAndTime2(time1,time2);
            if (monthOrders.size() != 0) {
                for (MonthOrder monthOrder : monthOrders) {
                    SystemUser systemUser = systemUserMapper.selectByPrimaryKey(monthOrder.getUserId());
                    monthOrder.setUserName(systemUser.getName());
                }
            }
            PageInfo<MonthOrder> systemUserPageInfo = new PageInfo<>(monthOrders);
            return new PageResultDTO(systemUserPageInfo.getTotal(), systemUserPageInfo.getList());
        }
        PageHelper.startPage(page, rows);
        Example example = new Example(MonthOrder.class);
        example.setOrderByClause(" time desc ");
        List<MonthOrder> monthOrders = monthOrderMapper.selectByExample(example);
        if (monthOrders.size() != 0) {
            for (MonthOrder monthOrder : monthOrders) {
                SystemUser systemUser = systemUserMapper.selectByPrimaryKey(monthOrder.getUserId());
                monthOrder.setUserName(systemUser.getName());
            }
        }
        PageInfo<MonthOrder> systemUserPageInfo = new PageInfo<>(monthOrders);
        return new PageResultDTO(systemUserPageInfo.getTotal(), systemUserPageInfo.getList());
    }

    @RequestMapping("tz")
    public String userTz() {
        return "tgls/qdAPI";
    }
}
