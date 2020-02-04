package com.haozhi.item.service;

import com.haozhi.item.dao.VipMapper;
import com.haozhi.item.pojo.Vip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/13 17:47
 */
@Service
public class VipService {

    @Autowired
    private VipMapper vipMapper;

    public List<Vip> queryAll() {
        return vipMapper.selectAll();
    }
}
