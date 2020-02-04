package com.haozhi.item.web.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.haozhi.common.constants.StatusCode;
import com.haozhi.common.dto.ResultDTO;
import com.haozhi.item.pojo.WinXinEntity;
import com.haozhi.item.utils.WXUnitl;
import com.haozhi.item.utils.WeiXinUnitl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
 
@Controller
@RequestMapping(value = "/weixin")
public class WeiXinController {
 
	@RequestMapping(value = "sgture",method = RequestMethod.POST)
	@ResponseBody
	public ResultDTO sgture(HttpServletRequest request) {
		String strUrl=request.getParameter("url");
		WinXinEntity wx = WeiXinUnitl.getWinXinEntity(strUrl);
		// 将wx的信息到给页面
		Map<String, Object> map = new HashMap<String, Object>();
		String sgture = WXUnitl.getSignature(wx.getTicket(), wx.getNoncestr(), wx.getTimestamp(), strUrl);
		map.put("signature", sgture.trim());//签名
		map.put("timestamp", wx.getTimestamp().trim());//时间戳
		map.put("noncestr",  wx.getNoncestr().trim());//随即串
        /*map.put("appid","wx061b55a767d388d8");//appID*/
		return new ResultDTO(true, StatusCode.OK,"查询成功",map);
	}
 
}