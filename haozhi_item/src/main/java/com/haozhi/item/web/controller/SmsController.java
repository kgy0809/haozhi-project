package com.haozhi.item.web.controller;

import com.haozhi.common.constants.StatusCode;
import com.haozhi.common.dto.ResultDTO;
import com.haozhi.item.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/11 11:21
 */
@Controller
@RequestMapping("sms")
public class SmsController {

    @Autowired
    private SmsService smsService;

    @RequestMapping(value = "send",method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO sendCode (String tel){
        smsService.sendCode(tel);
        return new ResultDTO(true, StatusCode.OK,"发送成功");
    }
}
