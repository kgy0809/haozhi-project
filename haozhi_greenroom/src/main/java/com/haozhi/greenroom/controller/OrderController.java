package com.haozhi.greenroom.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.haozhi.common.constants.StatusCode;
import com.haozhi.common.dto.PageResultDTO;
import com.haozhi.common.dto.ResultDTO;
import com.haozhi.greenroom.dao.*;
import com.haozhi.greenroom.pojo.*;
import com.haozhi.greenroom.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/12 15:55
 */
@Controller
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private HzUserMapper hzUserMapper;
    @Autowired
    private BusinessTwoMapper businessTwoMapper;
    @Autowired
    private HzYwMapper hzYwMapper;
    @Autowired
    private HzMenuMapper menuMapper;

    @RequestMapping(value = "xx", method = RequestMethod.GET)
    public String xx() {
        return "tgls/system/order/xxorder_list";
    }

    @RequestMapping(value = "xx", method = RequestMethod.POST)
    @ResponseBody
    public PageResultDTO queryOrder(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "10") Integer rows
    ) {
        PageHelper.startPage(page, rows);
        List<Order> orders = orderService.queryOrder();
        List<Order> list = new ArrayList<>();
        for (Order order : orders) {
            HzUser user = hzUserMapper.selectByPrimaryKey(order.getUserId());
            order.setUserId(user.getName());
            list.add(order);
        }
        PageInfo<Order> orderPageInfo = new PageInfo<>(list);
        return new PageResultDTO(orderPageInfo.getTotal(), orderPageInfo.getList());
    }

    /**
     * 修改线下（确认付款）
     *
     * @return
     */
    @RequestMapping(value = "xx/update",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> xxUpdate(String id) {
        orderService.updateOrder(id);
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "成功");
        return map;
    }

    /**
     * 修改线下审核
     *
     * @return
     */
    @RequestMapping(value = "xx/update/sh",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> xxShUpdate(String id) {
        orderService.updateShOrder(id);
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "成功");
        return map;
    }



    /**
     * 查看 xx
     *
     * @return
     */
    @RequestMapping("xx/view")
    public String xxView(String id, Map<String, Object> map) {
        Order order = orderService.queryOrderById(id);
        LastDto lastDto = businessLast(order.getPOrder());
        map.put("order", order);
        map.put("business", lastDto);
        return "tgls/system/order/xxorder_view";
    }

    public LastDto businessLast(String twoId) {
        BusinessTwo businessTwo = businessTwoMapper.selectByPrimaryKey(twoId);
        LastDto lastDto = new LastDto();
        BeanUtils.copyProperties(businessTwo, lastDto);
        String oneId = businessTwo.getOneId();
        HzYw hzYw = hzYwMapper.selectByPrimaryKey(oneId);
        lastDto.setHzYw(hzYw);
        lastDto.setSqrMessage(hzYw.getName());
        if (businessTwo.getMenuId() != null){
            String[] split = businessTwo.getMenuId().split(",");
            if (split.length > 0) {
                HzMenu key = menuMapper.selectByPrimaryKey(split[0]);
                HzMenu key1 = menuMapper.selectByPrimaryKey(key.getPid());
                if (key1 != null) {
                    HzMenu key2 = menuMapper.selectByPrimaryKey(key1.getPid());
                    lastDto.setOneName(key2.getName());
                }
            }
            List<HzMenu> list = new ArrayList<>();
            for (String s : split) {
                HzMenu menu = menuMapper.selectByPrimaryKey(s);
                list.add(menu);
            }
            lastDto.setMenuName(list);
        }
        return lastDto;
    }

    @RequestMapping(value = "wx", method = RequestMethod.GET)
    public String queryWxOrder() {
        return "tgls/system/order/wxorder_list";
    }

    /**
     * wx 支付
     *
     * @param
     * @return
     */
    @RequestMapping(value = "wx", method = RequestMethod.POST)
    @ResponseBody
    public PageResultDTO wx(@RequestParam(value = "page", defaultValue = "1") Integer page,
                            @RequestParam(value = "rows", defaultValue = "10") Integer rows) {
        PageHelper.startPage(page, rows);
        List<Order> orders = orderService.queryWxOrder();
        List<Order> list = new ArrayList<>();
        for (Order order : orders) {
            HzUser user = hzUserMapper.selectByPrimaryKey(order.getUserId());
            order.setUserId(user.getName());
            list.add(order);
        }
        PageInfo<Order> orderPageInfo = new PageInfo<>(list);
        return new PageResultDTO(orderPageInfo.getTotal(), orderPageInfo.getList());
    }

    /**
     * 修改wx
     *
     * @return
     */
    @RequestMapping(value = "wx/update",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> wxUpdate(String id) {
        orderService.wxUpdate(id);
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "成功");
        return map;
    }

    /**
     * ysh 支付
     *
     * @param
     * @return
     */
    @RequestMapping(value = "ysh", method = RequestMethod.GET)
    public String queryYshOrder() {
        return "tgls/system/order/yshorder_list";
    }

    @RequestMapping(value = "ysh", method = RequestMethod.POST)
    @ResponseBody
    public PageResultDTO ysh(@RequestParam(value = "page", defaultValue = "1") Integer page,
                             @RequestParam(value = "rows", defaultValue = "10") Integer rows) {
        PageHelper.startPage(page, rows);
        List<Order> orders = orderService.queryYshOrder();
        List<Order> list = new ArrayList<>();
        for (Order order : orders) {
            HzUser user = hzUserMapper.selectByPrimaryKey(order.getUserId());
            order.setUserId(user.getName());
            list.add(order);
        }
        PageInfo<Order> orderPageInfo = new PageInfo<>(list);
        return new PageResultDTO(orderPageInfo.getTotal(), orderPageInfo.getList());
    }

    /**
     * ytk
     *
     * @param
     * @return
     */
    @RequestMapping(value = "ytk", method = RequestMethod.GET)
    public String queryYtkOrder() {
        return "tgls/system/order/ytkorder_list";
    }

    @RequestMapping(value = "ytk", method = RequestMethod.POST)
    @ResponseBody
    public PageResultDTO ytk(@RequestParam(value = "page", defaultValue = "1") Integer page,
                             @RequestParam(value = "rows", defaultValue = "10") Integer rows) {
        PageHelper.startPage(page,rows);
        List<Order> orders = orderService.queryYtkOrder();
        List<Order> list = new ArrayList<>();
        for (Order order : orders) {
            HzUser user = hzUserMapper.selectByPrimaryKey(order.getUserId());
            order.setUserId(user.getName());
            list.add(order);
        }
        PageInfo<Order> orderPageInfo = new PageInfo<>(list);
        return new PageResultDTO(orderPageInfo.getTotal(),orderPageInfo.getList());
    }

    /**
     * 修改wx
     *
     * @return
     */
    @RequestMapping(value = "tk",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> tkUpdate(String id) {
        orderService.tkUpdate(id);
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "成功");
        return map;
    }
}
