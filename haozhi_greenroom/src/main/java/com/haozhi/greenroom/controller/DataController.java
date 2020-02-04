package com.haozhi.greenroom.controller;

import com.haozhi.greenroom.pojo.DataInof;
import com.haozhi.greenroom.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/12 12:29
 */
@Controller
@RequestMapping("dataInfo")
public class DataController {

    @Autowired
    private DataService dataService;

    /**
     * 查询全部
     *
     * @param map
     * @return
     */
    @RequestMapping
    public String queryData(Map<String, Object> map) {
        List<DataInof> dataInofs = dataService.queryData();
        map.put("data", dataInofs);
        return "tgls/system/dataInfo/dataInfo_list";
    }

    /**
     * 修改信息 回显
     *
     * @param id
     * @return
     */
    @RequestMapping("update")
    public String dataUpdate(String id, Map<String, Object> map) {
        DataInof dataInof = dataService.queryDataById(id);
        map.put("dataInof", dataInof);
        return "tgls/system/dataInfo/dataInfo_update";
    }

    @RequestMapping(value = "update/info", method = RequestMethod.POST)
    public String dataUpdateInfo(DataInof dataInof) {
        if (dataInof.getId() == null){
            //新增
            dataService.addDataInfo(dataInof);
        }else {
            //修改
            dataService.updateInfo(dataInof);
        }
        return "redirect:/dataInfo";
    }

    @DeleteMapping("delete/{id}")
    public Map<String, Object> dataUpdateDelete(@PathVariable("id") String pid) {
        dataService.dataUpdateDelete(pid);
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "成功");
        return map;
    }
    /**
     * 跳转新增
     * @return
     */
    @GetMapping("update/add")
    public String update(){
        return "tgls/system/dataInfo/dataInfo_add";
    }

}
