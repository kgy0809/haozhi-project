package com.haozhi.greenroom.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.haozhi.common.enums.ExceptionEnum;
import com.haozhi.common.exception.LyException;
import com.haozhi.common.utils.IdWorker;
import com.haozhi.greenroom.dao.UploadMapper;
import com.haozhi.greenroom.pojo.Banner;
import com.haozhi.greenroom.utils.QiniuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @author kgy
 * @version 1.0
 * @date 2019/12/26 10:08
 */
@Service
public class UploadService {

    @Autowired
    private UploadMapper uploadRepository;

    @Autowired
    private IdWorker idWorker;


    public PageInfo<Banner> listImage(Integer page,Integer rows) {
        PageHelper.startPage(page,rows);
        List<Banner> banners = uploadRepository.selectAll();
        return new PageInfo<>(banners);
    }

    @Transactional
    public void deleteImage(String pid) {
        uploadRepository.deleteByPrimaryKey(pid);
    }

    /**
     * 轮播图上传到七牛云
     * @param file
     * @return
     */
    public String addImage(MultipartFile file) {
        try {
            return new QiniuUtil().upload(UUID.randomUUID().toString().replace("-",""), file.getBytes());
        } catch (IOException e) {
            throw new LyException(ExceptionEnum.FILE_UPLOAD_ERROR);
        }
    }

    public void saveImage(String image) {
        Banner banner = new Banner();
        banner.setId(idWorker.nextId()+"");
        banner.setImage(image);
        uploadRepository.insert(banner);
    }
}
