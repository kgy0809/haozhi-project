package com.haozhi.item.web.controller;

import com.haozhi.item.pojo.Banner;
import com.haozhi.item.pojo.HzZc;
import com.haozhi.item.service.BannerService;
import com.haozhi.item.service.HzZcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * @author kgy
 * @version 1.0
 * @date 2019/12/26 15:59
 */
@Controller
@RequestMapping("/index")
@CrossOrigin
public class IndexController {
    @Autowired
    private BannerService bannerService;
    @Autowired
    private HzZcService hzZcService;
    /**
     * 页面进行跳转
     * @return
     */
    @RequestMapping
    public String index(Map<String,Object> map){
        List<Banner> bannerList = bannerService.findBannerList();
        List<HzZc> zcList = hzZcService.findZcList();
        map.put("labelList",bannerList);
        map.put("zcList",zcList);
        return "index/index";
    }

    @RequestMapping("skip")
    public String skip(){
        return "redirect:/index/index";
    }


}
