package com.haozhi.greenroom.controller;

import com.haozhi.greenroom.dao.VipMapper;
import com.haozhi.greenroom.pojo.Vip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/14 14:34
 */
@Controller
@RequestMapping("vip")
public class VipController {

    @Autowired
    private VipMapper vipMapper;

    @RequestMapping
    public String queryVip(Map<String, Object> map) {
        List<Vip> vips = vipMapper.selectAll();
        map.put("vip", vips);
        return "tgls/system/vip/vip_list";
    }

    @RequestMapping("update")
    public String updateVip(String id, Map<String, Object> map) {
        Vip vip = vipMapper.selectByPrimaryKey(id);
        map.put("vip", vip);
        return "tgls/system/vip/vip_update";
    }

    @DeleteMapping("delete/{id}")
    public Map<String, Object> dataUpdateDelete(@PathVariable("id") String pid) {
        vipMapper.deleteByPrimaryKey(pid);
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "成功");
        return map;
    }

    @RequestMapping(value = "update/info", method = RequestMethod.POST)
    public String insertVip(Vip vip) {
        vipMapper.updateByPrimaryKeySelective(vip);
        return "redirect:/vip";
    }
}
