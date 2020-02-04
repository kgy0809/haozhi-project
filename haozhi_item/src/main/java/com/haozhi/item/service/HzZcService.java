package com.haozhi.item.service;

import com.haozhi.item.dao.HzZcRepository;
import com.haozhi.item.pojo.HzZc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author kgy
 * @version 1.0
 * @date 2019/12/27 10:03
 */
@Service
public class HzZcService {

    @Autowired
    private HzZcRepository hzZcRepository;

    /**
     * 查询首页的zc
     * @return
     */
    public List<HzZc> findZcList() {
        return hzZcRepository.selectAll();
    }
}
