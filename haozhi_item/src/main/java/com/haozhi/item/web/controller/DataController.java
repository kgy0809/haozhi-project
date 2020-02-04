package com.haozhi.item.web.controller;

import com.haozhi.common.constants.StatusCode;
import com.haozhi.common.dto.ResultDTO;
import com.haozhi.item.dto.DataTextDto;
import com.haozhi.item.pojo.DataText;
import com.haozhi.item.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author kgy
 * @version 1.0
 * @date 2019/12/26 11:10
 */
@Controller
@RequestMapping("data")
public class DataController {

    @Autowired
    private DataService dataService;




    /**
     * 查询所有的资料
     *
     * @return
     */
    @PostMapping("list")
    @ResponseBody
    public ResultDTO textList(Integer page, Integer size) {
        List<DataTextDto> list = dataService.textList(page, size);
        return new ResultDTO(true, StatusCode.OK, "查询成功",list);
    }

    /**
     * 根据id查询详情
     * @param id
     * @return
     */
    @GetMapping("list/{id}")
    public String textDetails(@PathVariable("id") String id, Map<String,Object> map) {
        DataText dataText = dataService.textDetails(id);
        dataText.setsTime(String.valueOf(dataText.getTime()));
        map.put("dataText",dataText);
        return "index/data_detail";
    }
}
