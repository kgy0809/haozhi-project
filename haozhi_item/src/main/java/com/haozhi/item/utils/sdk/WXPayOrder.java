package com.haozhi.item.utils.sdk;

import com.haozhi.item.pojo.Order;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WXPayOrder {

    private static Map<String, String> data;
    private static MyConfig config;
    private static WXPay wxPay;
    private static Order order;
    private static WXPayOrder wxPayOrder;

    private static final String PRODUCT_NAME = "好智（天津）企业管理咨询有限责任公司";
    private static final String NOTIFY_URL = "http://haozhiqiye.haozhizixun.com/api/pay/notify";
    private static final String NOTIFY_URL_VIP = "http://haozhiqiye.haozhizixun.com/api/pay/notify/vip";

    public WXPayOrder(Order order) {
        this.order = order;
    }

    public Map<String, Object> getParameter() throws Exception {
        Map<String, Object> result = new HashMap<>();
        try {
            config = new MyConfig();
            wxPay = new WXPay(config);
            data = new HashMap<String, String>();
            /**
             * 下单商品名称
             */
            data.put("body", "111");
            /**
             * 商品订单号
             */
            data.put("out_trade_no", order.getId());
            /**
             * 标价币种
             */
            data.put("fee_type", "CNY");
            /**
             * 用户标识
             */
            data.put("openid", order.getOpenid());
            /**
             * 标价金额
             */
            data.put("total_fee", (1 + ""));
            /**
             * 通知地址
             */
            if ("10086".equals(order.getVipState())) {
                data.put("notify_url", NOTIFY_URL_VIP);
            } else {
                data.put("notify_url", NOTIFY_URL);
            }
            /**
             * 交易类型
             */
            data.put("trade_type", "JSAPI");
            /**
             * 微信sdk集成方法, 统一下单接口unifiedOrder, 此处请求   MD5加密   加密方式
             */
            Map<String, String> response = wxPay.unifiedOrder(data);
            //        logger.info("微信支付下单成功, 返回值 response={}", response);
            String returnCode = response.get("return_code");
            if (!"SUCCESS".equals(returnCode)) {
                return null;
            }
            String resultCode = response.get("result_code");
            if (!"SUCCESS".equals(resultCode)) {
                return null;
            }
            String prepay_id = response.get("prepay_id");
            if (prepay_id == null) {
                return null;
            }

            // ******************************************
            //
            //  前端调起微信支付必要参数
            //
            // ******************************************
            String packages = "prepay_id=" + prepay_id;
            Map<String, String> wxPayMap = new HashMap<String, String>();
            wxPayMap.put("appId", config.getAppID());
            int nowTime = (int) (new Date().getTime() / 1000);
            wxPayMap.put("timeStamp", nowTime + "");
            String uuidStr = UUID.randomUUID().toString().replace("-", "");
            wxPayMap.put("nonceStr", uuidStr);
            wxPayMap.put("package", packages);
            wxPayMap.put("signType", "MD5");
            // 加密串中包括 appId timeStamp nonceStr package signType 5个参数, 通过sdk WXPayUtil类加密, 注意, 此处使用  MD5加密  方式
            String sign = WXPayUtil.generateSignature(wxPayMap, config.getKey());

            // ******************************************
            //
            //  返回给前端调起微信支付的必要参数
            //
            // ******************************************
            result.put("sign", sign);
            result.putAll(wxPayMap);

        } catch (Exception e) {
            e.getMessage();
        }
        return result;

    }

}