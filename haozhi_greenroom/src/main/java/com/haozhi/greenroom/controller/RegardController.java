package com.haozhi.greenroom.controller;

import com.haozhi.greenroom.dao.RegardMapper;
import com.haozhi.greenroom.pojo.Regard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/17 16:49
 */
@Controller
@RequestMapping("regard")
public class RegardController {

    @Autowired
    private RegardMapper regardMapper;

    @RequestMapping
    public String regard(Map<String, Object> map) {
        List<Regard> regards = regardMapper.selectAll();
        map.put("regard",regards.get(0));
        return "tgls/system/regard/regard_update";
    }

    @RequestMapping("update")
    public String regardUpdate(Regard regard){
        regardMapper.updateByPrimaryKeySelective(regard);
        return "redirect:/regard";
    }
}
