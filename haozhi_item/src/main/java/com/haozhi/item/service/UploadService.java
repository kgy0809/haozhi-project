package com.haozhi.item.service;

import com.haozhi.common.enums.ExceptionEnum;
import com.haozhi.common.exception.LyException;
import com.haozhi.item.utils.QiniuUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @author kgy
 * @version 1.0
 * @date 2019/12/27 18:13
 */
@Controller
public class UploadService {

    public String insertImage(MultipartFile file) {
        try {
            return new QiniuUtil().upload(UUID.randomUUID().toString().replace("-",""), file.getBytes());
        } catch (IOException e) {
            throw new LyException(ExceptionEnum.FILE_UPLOAD_ERROR);
        }
    }
}
