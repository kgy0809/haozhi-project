package com.haozhi.greenroom.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.haozhi.common.dto.PageResultDTO;
import com.haozhi.greenroom.dao.SystemUserMapper;
import com.haozhi.greenroom.dao.VipOrderMapper;
import com.haozhi.greenroom.pojo.Account;
import com.haozhi.greenroom.pojo.SystemUser;
import com.haozhi.greenroom.pojo.VipOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("viporder")
public class VipOrderController {

    @Autowired
    private VipOrderMapper vipOrderMapper;
    @Autowired
    private SystemUserMapper systemUserMapper;

    @RequestMapping(method = RequestMethod.GET)
    public String queryVipOrder() {
        return "tgls/system/vip/vip_order_list";
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
            Example example = new Example(VipOrder.class);
            example.createCriteria().andEqualTo("userId", uid).andEqualTo("state", "1");
            example.setOrderByClause(" time desc");
            List<VipOrder> vipOrders = vipOrderMapper.selectByExample(example);
            for (VipOrder vipOrder : vipOrders) {
                SystemUser systemUser = systemUserMapper.selectByPrimaryKey(vipOrder.getUserId());
                vipOrder.setUserName(systemUser.getName());
            }
            return new PageResultDTO((long) vipOrders.size(), vipOrders);
        }
        if (id != null && !("").equals(id)) {
            VipOrder vipOrder = vipOrderMapper.selectByPrimaryKey(id);
            if (vipOrder != null){
                SystemUser systemUser = systemUserMapper.selectByPrimaryKey(vipOrder.getUserId());
                vipOrder.setUserName(systemUser.getName());
            }
            List<VipOrder> list = new ArrayList<>();
            list.add(vipOrder);
            return new PageResultDTO((long) list.size(), list);
        }
        if ((time1 != null && !("").equals(time1)) || (time2 != null && !("").equals(time2))) {
            PageHelper.startPage(page, rows);
            List<VipOrder> list = vipOrderMapper.selectByTime1AndTime2(time1, time2);
            if (list.size() != 0) {
                for (VipOrder vipOrder : list) {
                    SystemUser systemUser = systemUserMapper.selectByPrimaryKey(vipOrder.getUserId());
                    vipOrder.setUserName(systemUser.getName());
                }
            }
            PageInfo<VipOrder> accountPageInfo = new PageInfo<>(list);
            return new PageResultDTO(accountPageInfo.getTotal(), accountPageInfo.getList());
        }
        PageHelper.startPage(page, rows);
        Example example = new Example(VipOrder.class);
        example.createCriteria().andEqualTo("state", "1");
        example.setOrderByClause(" time desc");
        List<VipOrder> vipOrders = vipOrderMapper.selectByExample(example);
        for (VipOrder vipOrder : vipOrders) {
            SystemUser systemUser = systemUserMapper.selectByPrimaryKey(vipOrder.getUserId());
            vipOrder.setUserName(systemUser.getName());
        }
        PageInfo<VipOrder> accountPageInfo = new PageInfo<>(vipOrders);
        return new PageResultDTO(accountPageInfo.getTotal(), accountPageInfo.getList());
    }

}
