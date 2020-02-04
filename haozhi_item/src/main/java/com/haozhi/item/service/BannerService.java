package com.haozhi.item.service;

import com.haozhi.common.utils.IdWorker;
import com.haozhi.item.dao.BannerRepository;
import com.haozhi.item.pojo.Banner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author kgy
 * @version 1.0
 * @date 2019/12/26 9:17
 */
@Service
public class BannerService {

    @Autowired
    private IdWorker idWorker;
    @Autowired
    private BannerRepository bannerRepository;

    public List<Banner> findBannerList() {
        return bannerRepository.selectAll();
    }
}
