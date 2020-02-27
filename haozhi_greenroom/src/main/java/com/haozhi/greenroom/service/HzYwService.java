package com.haozhi.greenroom.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.haozhi.greenroom.dao.HzYwMapper;
import com.haozhi.greenroom.pojo.HzYw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/12 14:49
 */
@Service
public class HzYwService {

    @Autowired
    private HzYwMapper hzYwMapper;

    public List<HzYw> queryAll() {

        return hzYwMapper.selectAll();
    }

    public HzYw queryById(String id) {
        return hzYwMapper.selectByPrimaryKey(id);
    }

    public PageInfo<HzYw> listImage(Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        List<HzYw> list = hzYwMapper.selectAll();
        return new PageInfo<HzYw>(list);
    }

    public void updateData(HzYw hzYw) {
        if (hzYw.getGfPrice() == -1) {
            hzYw.setGfPrice(-1);
        } else {
            hzYw.setGfPrice(hzYw.getGfPrice() * 100);
        }
        hzYw.setVipPrice(hzYw.getVipPrice() * 100);
        hzYw.setHyPrice(hzYw.getHyPrice() * 100);
        hzYwMapper.updateByPrimaryKeySelective(hzYw);
    }
}
