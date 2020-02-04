package com.haozhi.greenroom.service;

import com.haozhi.greenroom.dao.MenuMapper;
import com.haozhi.greenroom.pojo.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/12 10:12
 */
@Service
public class MenuService {

    @Autowired
    private MenuMapper menuMapper;

    public List<Menu> queryMenu() {
/*        Example example = new Example(Menu.class);
        example.createCriteria().andEqualTo("pid",id);
        return menuMapper.selectByExample(example);*/
    return menuMapper.selectAll();
    }
}
