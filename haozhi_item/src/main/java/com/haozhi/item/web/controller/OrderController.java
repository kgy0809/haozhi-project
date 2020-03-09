package com.haozhi.item.web.controller;

import com.haozhi.common.constants.StatusCode;
import com.haozhi.common.dto.ResultDTO;
import com.haozhi.common.utils.IdWorker;
import com.haozhi.item.pojo.Order;
import com.haozhi.item.service.OrderService;
import com.haozhi.item.utils.QiniuUtil;
import com.haozhi.item.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/4 9:34
 */
@Controller
@RequestMapping("order")
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private IdWorker idWorker;

    @RequestMapping(value = "all", method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO allOrder(@RequestParam(name = "type", required = true) String type, @RequestParam(name = "page", defaultValue = "1") Integer page, @RequestParam(name = "rows", defaultValue = "10") Integer rows) {

        List<Order> orders = orderService.allOrder(type, page, rows, getUser().getId());
        if (orders == null)
            return new ResultDTO(true, StatusCode.ERROR, "type传参有错");
        return new ResultDTO(true, StatusCode.OK, "查询成功", orders);
    }

    @RequestMapping("upload")
    public String upload(String id, Map<String, Object> map) {
        Order order = orderService.queryById(id);
        if ((order.getPower() != null && !order.getPower().equals("")) || (order.getContract() != null && !order.getContract().equals(""))) {
            map.put("order", order);
            return "update_upload";
        }
        map.put("order", order);
        map.put("id", id);
        return "Upload";
    }

    @RequestMapping(value = "upload/file", method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO uploadFile(MultipartFile file) throws IOException {
        String upload = new QiniuUtil().upload(idWorker.nextId() + "", file.getBytes());
        return new ResultDTO(true, StatusCode.OK, "上传成功", upload);
    }

}
