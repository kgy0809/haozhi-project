package com.haozhi.item.web.controller;

import com.haozhi.item.dto.NumDto;
import com.haozhi.item.pojo.Order;
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
 * @date 2019/12/26 18:27
 */
@Controller
@RequestMapping("progress")
public class ProgressController extends BaseController {

    @Autowired
    private OrderService orderService;

    @RequestMapping
    public String progress(Map<String, Object> map) {
        NumDto numDto = orderService.queryCountNum(getUser().getId());
        map.put("numDto", numDto);
        return "schedule/schedule";
    }

    @RequestMapping("myOrder")
    public String myOrder() {
        return "order/my_order";
    }

    @RequestMapping(value = "orderurl", method = RequestMethod.POST)
    public String uploadFileUrl(Order order) {
        orderService.uploadFileUrl(order);
        return "redirect:/progress";
    }


    @RequestMapping("orderIndex")
    public String orderIndex(Map<String, Object> map) {
        NumDto numDto = orderService.queryCountNum(getUser().getId());
        map.put("numDto", numDto);
        return "schedule/schedule";
    }
}
