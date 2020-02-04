package com.haozhi.item.web.controller;

import com.haozhi.common.constants.StatusCode;
import com.haozhi.common.dto.ResultDTO;
import com.haozhi.item.pojo.Banner;
import com.haozhi.item.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author kgy
 * @version 1.0
 * @date 2019/12/26 9:18
 */
@Controller
@RequestMapping("image")
@CrossOrigin
public class BannerController {

    @Autowired
    private BannerService bannerService;

    /**
     * 查询所有轮播图
     * @return
     */
    @RequestMapping("banner")
    public ResultDTO list(){
        List<Banner> list = bannerService.findBannerList();
        return new ResultDTO(true, StatusCode.OK,"查询成功",list);
    }


}
