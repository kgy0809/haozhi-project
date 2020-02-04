package com.haozhi.greenroom.controller;

import com.haozhi.greenroom.dao.HzMenuMapper;
import com.haozhi.greenroom.dao.HzZcMapper;
import com.haozhi.greenroom.pojo.HzMenu;
import com.haozhi.greenroom.pojo.HzYw;
import com.haozhi.greenroom.pojo.HzZc;
import com.haozhi.greenroom.utils.Excel;
import com.haozhi.greenroom.utils.ExcelUtil;
import com.haozhi.greenroom.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/15 16:46
 */
@Controller
@RequestMapping("menu")
public class MenuController {

    @Autowired
    private HzMenuMapper hzMenuMapper;
    @Autowired
    private HzZcMapper hzZcMapper;

    @RequestMapping
    public String menu(Map<String, Object> map) {
        List<HzZc> hzZcs = hzZcMapper.selectAll();
        map.put("HzZc",hzZcs);
        return "tgls/system/menu/menu_list";
    }

    @RequestMapping("add")
    public String menu() {
        return "tgls/system/menu/menu";
    }

    @RequestMapping("add/file")
    public String menu(MultipartFile file) throws Exception {
        File toFile = FileUtil.multipartFileToFile(file);
        List<HzMenu> hzYws = ExcelUtil.importExcel(toFile, HzMenu.class, 1);
        hzMenuMapper.delete(new HzMenu());
        hzYws.forEach(hzYw -> {
            hzMenuMapper.insert(hzYw);
        });
        return "redirect:/menu/add";
    }

    @RequestMapping("update")
    public String menuUpdate(String id, Map<String, Object> map) {
        HzZc hzZc = hzZcMapper.selectByPrimaryKey(id);
        map.put("hzZc", hzZc);
        return "tgls/system/menu/menu_update";
    }

    @RequestMapping("update/add")
    public String menuUpdate(HzZc hzZc) {
        hzZcMapper.updateByPrimaryKeySelective(hzZc);
        return "redirect:/menu";
    }

}
