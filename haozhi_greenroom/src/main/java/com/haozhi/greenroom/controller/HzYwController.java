package com.haozhi.greenroom.controller;

import com.github.pagehelper.PageInfo;
import com.haozhi.common.constants.StatusCode;
import com.haozhi.common.dto.PageResultDTO;
import com.haozhi.common.dto.ResultDTO;
import com.haozhi.common.utils.IdWorker;
import com.haozhi.greenroom.pojo.Banner;
import com.haozhi.greenroom.pojo.HzMenu;
import com.haozhi.greenroom.pojo.HzYw;
import com.haozhi.greenroom.service.HzYwService;
import com.haozhi.greenroom.utils.ExcelUtil;
import com.haozhi.greenroom.utils.FileUtil;
import com.haozhi.greenroom.utils.QiniuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/12 14:48
 */
@Controller
@RequestMapping("brand")
public class HzYwController {

    @Autowired
    private HzYwService hzYwService;
    @Autowired
    private IdWorker idWorker;

    @RequestMapping
    public String asd() {
        return "tgls/system/brand/brand_list";
    }

    @RequestMapping(value = "fy", method = RequestMethod.POST)
    @ResponseBody
    public PageResultDTO upload(@RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "rows", defaultValue = "10") Integer rows, Map<String, Object> map) {

        PageInfo<HzYw> PageInfo = hzYwService.listImage(page, rows);
        return new PageResultDTO(PageInfo.getTotal(), PageInfo.getList());
    }

    @RequestMapping("update")
    public String queryById(String id, Map<String, Object> map) {
        HzYw hzYw = hzYwService.queryById(id);
        map.put("hzYw", hzYw);
        return "tgls/system/brand/brand_update";
    }

    @RequestMapping(value = "update/add", method = RequestMethod.POST)
    public String updateData(HzYw hzYw) {
        hzYwService.updateData(hzYw);
        return "redirect:/brand";
    }

    @RequestMapping("add/file")
    @ResponseBody
    public ResultDTO menu(MultipartFile file) throws Exception {
        String upload = new QiniuUtil().upload(idWorker.nextId() + ""+".doc", file.getBytes());
        return new ResultDTO(true, StatusCode.OK,"上传成功",upload);
    }
}
