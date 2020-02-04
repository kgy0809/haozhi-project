package com.haozhi.item.service;

import com.haozhi.item.dao.BusinessTwoMapper;
import com.haozhi.item.dao.HzYwRepository;
import com.haozhi.item.dao.MenuMapper;
import com.haozhi.item.dto.MenuDto;
import com.haozhi.item.pojo.BusinessTwo;
import com.haozhi.item.pojo.HzYw;
import com.haozhi.item.pojo.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kgy
 * @version 1.0
 * @date 2019/12/30 14:26
 */
@Service
public class MenuService {

    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private BusinessTwoMapper businessTwoMapper;
    @Autowired
    private HzYwRepository hzYwRepository;

    /**
     * 查询父节点
     *
     * @return
     */
    public List<Menu> queryOne() {
        Example example = new Example(Menu.class);
        example.createCriteria().andEqualTo("pid", 0);
        return menuMapper.selectByExample(example);
    }

    /**
     * 查询所有子节点
     *
     * @param id
     */
    public List<MenuDto> queryTwo(String id, String towId, String uid) {
        Example example = new Example(Menu.class);
        example.createCriteria().andEqualTo("pid", id);
        List<Menu> menus = menuMapper.selectByExample(example);
        List<MenuDto> list = new ArrayList();
        BusinessTwo businessTwo = businessTwoMapper.selectByPrimaryKey(towId);
        Example hz = new Example(HzYw.class);
        hz.createCriteria().andEqualTo("id", businessTwo.getOneId());
        HzYw hzYw = hzYwRepository.selectOneByExample(hz);
        Integer price1 = (hzYw.getGfPrice() == -1 ? 0 : hzYw.getGfPrice());
        Integer price2 = hzYw.getHyPrice();
        Integer price3 = hzYw.getVipPrice();
        for (Menu menu : menus) {
            MenuDto menuDto = new MenuDto();
            /**
             * tow
             */
            menuDto.setMenuTwo(menu);
            Example wx = new Example(Menu.class);
            wx.createCriteria().andEqualTo("pid", menu.getId());
            List<Menu> menuThree = menuMapper.selectByExample(wx);
            menuDto.setMenuThree(menuThree);
            if (("1").equals(uid)) {
                menuDto.setZjPrice(price1 + price2);
            } else {
                menuDto.setZjPrice(price1 + price3);
            }
            list.add(menuDto);
        }

        return list;
    }

}
