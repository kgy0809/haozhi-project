package com.haozhi.item.web.controller;

import com.haozhi.common.constants.StatusCode;
import com.haozhi.common.dto.ResultDTO;
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
    @ResponseBody
    public ResultDTO uploadFileUrl(String id, String htString, String wtSting) {
        Order order = new Order();
        order.setId(id);
        order.setPower(wtSting);
        order.setContract(htString);
        orderService.uploadFileUrl(order);
        return new ResultDTO(true, StatusCode.OK, "上传成功");
    }

    @RequestMapping(value = "orderurl/xg", method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO uploadFileUrlXg(String id, String htString, String wtSting) {
        Order orderOld = orderService.uploadFileUrlXg(id);
        Order order = new Order();
        if (!"".equals(htString)) {
            if (!"".equals(orderOld.getContract())) {
                String concat = orderOld.getContract().concat(",");
                String newContract = concat.concat(htString);
                order.setContract(newContract);
            }
        }
        if (!"".equals(wtSting)) {
            if (!"".equals(orderOld.getPower())) {
                String concat = orderOld.getPower().concat(",");
                String newPower = concat.concat(wtSting);
                order.setPower(newPower);
            }
        }
        order.setId(id);
        orderService.uploadFileUrl(order);
        return new ResultDTO(true, StatusCode.OK, "上传成功");
    }

    @RequestMapping("orderIndex")
    public String orderIndex(Map<String, Object> map) {
        NumDto numDto = orderService.queryCountNum(getUser().getId());
        map.put("numDto", numDto);
        return "schedule/schedule";
    }
}
