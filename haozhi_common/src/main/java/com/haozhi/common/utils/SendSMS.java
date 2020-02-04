package com.haozhi.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SendSMS {
    private static final String URL = "http://www.ztsms.cn/sendNSms.do";
        private static final String NAME = "haozhi88";
    private static final String PASSWORD = "Cxz307312";
    private String mobile;
    private String random;
    private String content;

    public SendSMS(String mobile) {
        this.random = (int) ((Math.random() * 1000) + 9999) + "";
        content = "你的验证码为" + random + "如无操作请忽略【好智企业】";
        this.mobile = mobile;
    }

    public String getSend() {
        String tkey = TimeUtil.getNowTime("yyyyMMddHHmmss");
        String xh = "";
        try {
            content = URLEncoder.encode(content, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String param = "url=" + URL + "&username=" + NAME + "&password=" + MD5Gen.getMD5(MD5Gen.getMD5(PASSWORD) + tkey) + "&tkey=" + tkey + "&productid=676767&mobile=" + this.mobile + "&content=" + content + "&xh" + xh;
/*        System.out.println(param);
        System.out.println(this.mobile);*/
        String ret = HttpRequest.sendPost(URL, param);
        return this.random;
    }
}
