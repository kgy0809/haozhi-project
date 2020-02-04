package com.haozhi.greenroom.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.haozhi.common.dto.PageResultDTO;
import com.haozhi.greenroom.pojo.Banner;
import com.haozhi.greenroom.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kgy
 * @version 1.0
 * @date 2019/12/26 10:06
 */
@Controller
@RequestMapping("upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @RequestMapping
    public String asd(){
        return "tgls/system/banner/list_upload";
    }

    /**
     * 查询所有的轮播图
     *
     * @param
     * @return
     */
    @RequestMapping(value = "fy",method = RequestMethod.POST)
    @ResponseBody
    public PageResultDTO upload(@RequestParam(value = "page",defaultValue = "1")Integer page, @RequestParam(value = "rows",defaultValue = "10") Integer rows) {

        PageInfo<Banner> PageInfo = uploadService.listImage(page, rows);
        return new PageResultDTO(PageInfo.getTotal(),PageInfo.getList());
    }

    /**
     * 跳转新增
     *
     * @return
     */
    @GetMapping("update")
    public String update() {
        return "tgls/system/banner/add_update";
    }

    /**
     * 新增轮播图
     *
     * @param file
     * @return
     */
    @PostMapping("insert")
    @ResponseBody
    public Map<String, Object> updateImage(@RequestParam(value = "file") MultipartFile file) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        if (file.getSize() == 0) {
            map.put("msg", "请添加图片！");
            map.put("code", 500);
        }
        String url = uploadService.addImage(file);
        map.put("src", url);
        return map;
    }

    @PostMapping("save")
    public String saveImage(String image) {
        uploadService.saveImage(image);
        return "redirect:/upload";
    }

    /**
     * 删除轮播图
     *
     * @param pid
     * @return
     */
    @DeleteMapping("delete/{id}")
    @ResponseBody
    public Map<String, Object> view(@PathVariable("id") String pid) {
        uploadService.deleteImage(pid);
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "成功");
        return map;
    }

    /**
     * 新增轮播图
     *
     * @param file
     * @return
     */
    @PostMapping("insert/fwb")
    @ResponseBody
    public Map<String, Object> updateImagefwb(MultipartFile file) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        if (file.getSize() == 0) {
            map.put("msg", "请添加图片！");
            map.put("code", 500);
        }
        map.put("code", 0);
        String url = uploadService.addImage(file);
        map.put("msg", "上传成功");
        map2.put("src", url);
        map.put("data", map2);
        return map;
    }

}
