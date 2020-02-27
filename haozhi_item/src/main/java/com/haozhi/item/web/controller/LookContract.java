package com.haozhi.item.web.controller;

import com.haozhi.common.constants.StatusCode;
import com.haozhi.common.dto.ResultDTO;
import com.haozhi.item.dto.LastDto;
import com.haozhi.item.pojo.BusinessTwo;
import com.haozhi.item.pojo.Order;
import com.haozhi.item.service.BusinessTwoService;
import com.haozhi.item.service.HzYwService;
import com.haozhi.item.service.OrderService;
import com.haozhi.item.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/13 15:54
 */
@Controller
@RequestMapping("mycan")
public class LookContract extends BaseController {

    @Autowired
    private BusinessTwoService businessTwoService;
    @Autowired
    private HzYwService hzYwService;
    @Autowired
    private OrderService orderService;

    @RequestMapping
    public String mycan(String id, Map<String, Object> map) {
        String mycan = businessTwoService.mycan(id);
        Order order = orderService.queryById(id);
        if (mycan == null) {
            return "没有合同";
        } else if (mycan.equals("10086")) {
            BusinessTwo etc = hzYwService.etc(order.getPOrder());
            etc.setStall("1");
            map.put("etc", etc);
            return "order/my_order_contract";
        } else if (mycan.equals("10087")) {
            BusinessTwo etc = hzYwService.etc(order.getPOrder());
            etc.setStall("1");
            map.put("etc", etc);
            return "flow/my_flow_contract";
        } else if (mycan.equals("10088")) {
            BusinessTwo etc = hzYwService.etc(order.getPOrder());
            etc.setStall("1");
            map.put("etc", etc);
            return "order/my_order_contract";
        } else if (mycan.equals("10089")) {
            BusinessTwo etc = hzYwService.etc(order.getPOrder());
            etc.setStall("1");
            map.put("etc", etc);
            return "case/my_case_contract";
        } else if (mycan.equals("10090")) {
            BusinessTwo etc = hzYwService.etc(order.getPOrder());
            etc.setStall("1");
            map.put("etc", etc);
            return "soft/my_soft_contract";
        } else if (mycan.equals("10091")) {
            BusinessTwo etc = hzYwService.etc(order.getPOrder());
            etc.setStall("1");
            map.put("etc", etc);
            return "copyright/my_copyright_contract";
        }
        return "没有合同供下载";
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO deleteMy(String id) {
        businessTwoService.deleteById(id);
        return new ResultDTO(true, StatusCode.OK, "取消成功");
    }

    @RequestMapping(value = "pay")
    public String mycanPay(Map<String, Object> map,String id) {
        Order order = orderService.queryById(id);
        String mycan = businessTwoService.mycanPay(id);
        if (mycan == null) {
            return "订单错误";
        } else if (mycan.equals("10086")) {
            LastDto lastDto = hzYwService.businessLast(order.getPOrder(),getUser());
            map.put("list", lastDto);
            return "civil/business_information";
        } else if (mycan.equals("10087")) {
            LastDto lastDto = hzYwService.updateLast(order.getPOrder());
            map.put("list",lastDto);
            return "flow/flow_information";
        } else if (mycan.equals("10088")) {
            LastDto lastDto = hzYwService.businessLast(order.getPOrder(),getUser());
            map.put("list", lastDto);
            return "civil/business_information";
        } else if (mycan.equals("10089")) {
            LastDto lastDto = hzYwService.updateLast(order.getPOrder());
            map.put("list",lastDto);
            return "case/case_information";
        } else if (mycan.equals("10090")) {
            LastDto lastDto = hzYwService.updateLast(order.getPOrder());
            map.put("list",lastDto);
            return "soft/solt_information";
        } else if (mycan.equals("10091")) {
            LastDto lastDto = hzYwService.updateLast(order.getPOrder());
            map.put("list",lastDto);
            return "copyright/right_information";
        }
        return null;
    }
}
