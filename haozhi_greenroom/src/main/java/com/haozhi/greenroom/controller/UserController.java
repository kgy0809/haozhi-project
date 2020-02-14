package com.haozhi.greenroom.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.haozhi.common.dto.PageResultDTO;
import com.haozhi.greenroom.dao.SystemUserMapper;
import com.haozhi.greenroom.pojo.SystemUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/12 15:04
 */
@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private SystemUserMapper systemUserMapper;

    @RequestMapping(method = RequestMethod.GET)
    public String queryUser(Map<String, Object> map) {
        List<SystemUser> systemUsers = systemUserMapper.selectAll();
        map.put("data", systemUsers);
        return "tgls/system/user/use_list";
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public PageResultDTO queryUser(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                   @RequestParam(value = "rows", defaultValue = "10") Integer rows) {
        PageHelper.startPage(page,rows);
        Example example = new Example(SystemUser.class);
        example.setOrderByClause("time desc");
        List<SystemUser> systemUsers = systemUserMapper.selectByExample(example);
        PageInfo<SystemUser> systemUserPageInfo = new PageInfo<>(systemUsers);
        return new PageResultDTO(systemUserPageInfo.getTotal(),systemUserPageInfo.getList());
    }

    @RequestMapping("tz")
    public String userTz() {
        return "tgls/qdAPI";
    }
}
