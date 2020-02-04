package com.haozhi.item.web.controller;

import com.haozhi.common.constants.StatusCode;
import com.haozhi.common.dto.ResultDTO;
import com.haozhi.item.pojo.User;
import com.haozhi.item.service.PayService;
import com.haozhi.item.utils.sdk.WXPayUtil;
import com.haozhi.item.web.common.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/11 8:33
 */
@Controller
@RequestMapping("pay")
public class PayController extends BaseController {

    @Autowired
    private PayService payService;

    /**
     *  订单支付 微信
     * @param bid
     * @param pay 支付金额
     * @param remark 备注
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO createOrder(String bid,Integer pay,String remark){
        Map<String, Object> order = payService.createOrder(getUser(), bid, pay, remark);
        return new ResultDTO(true, StatusCode.OK,"返回订单",order);
    }

    /**
     * 购买vip
     * @return
     */
    @RequestMapping(value = "vip",method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO createVip(){
        Map<String, Object> vip = payService.createVip(getUser());
        return new ResultDTO(true, StatusCode.OK,"返回订单",vip);
    }


    /**
     * 订单回调
     * @param request
     * @param response
     * @throws IOException
     */
    @PostMapping(value = "notify")
    public void notify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean flag = false;
        ServletInputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            is = request.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            StringBuilder stb = new StringBuilder();
            String s = "";
            while ((s = br.readLine()) != null) {
                stb.append(s);
            }
            Map<String, String> notifyMap = WXPayUtil.xmlToMap(stb.toString());//将微信发的xml转map
//            Document document = DocumentHelper.parseText(stb.toString());
//            String return_code = "";
//            List<Element> returnCodeList = document.selectNodes("//return_code");
//            for (Element element : returnCodeList) {
//                return_code = element.getText();
//            }
            if (notifyMap.get("return_code").equals("SUCCESS") || notifyMap.get("return_code").equals("01")) {    //返回成功
                if (notifyMap.get("result_code").equals("SUCCESS") || notifyMap.get("result_code").equals("01")) {
                    String out_trade_no = notifyMap.get("out_trade_no");//商户订单号

                    // TODO 处理订单
                    if (StringUtils.isNotBlank(out_trade_no)) {
                        //修改支付状态为已支状态                  appOrderServiceImpl.updateC(out_trade_no);
                        payService.queryOrder(out_trade_no);
                    }
                        flag = true;
                }
            }
        } catch (Exception e) {
            flag = false;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                flag = true;
            }
        }
        if (flag) {
            response.getWriter().println("<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>");
        } else {
            response.getWriter().println("<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[return_code_err]]></return_msg></xml>");
        }
    }

    /**
     * 线下支付 支付成功了 在后台修改订单 提成
     * @return
     */
    /*@RequestMapping(value = "other",method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO payOther(String bid,Integer pay){

    }*/

    @RequestMapping(value = "/xxPayOrder",method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO xxPayOrder(String bid,Integer pay,String remark){
        payService.xxPayOrder(getUser(), bid, pay, remark);
        return new ResultDTO(true,StatusCode.OK,"线下支付订单创建成功");
    }

}
