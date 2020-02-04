package com.haozhi.greenroom.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.stereotype.Component;

/**
 * @author kgy
 * @version 1.0
 * @date 2019/12/13 14:44
 */

@Component
public class QiniuUtil {

    private static final String ACCESSKEY = "3kTPdLpscWxQUZBFnAWBJ0pM7SEQpAfriRRgWlrY" ;

    private static final String SECRETKEY = "Xow_1k4wVyu4iJ-0QlbheEee7F6qZBK-pbYJ3zZv" ;

    private static final String BUCKETNAME = "haozhi-image" ;

    private static final String FILEDOMAIN = "http://q55l2dpen.bkt.clouddn.com/" ;

    private UploadManager manager;

    public QiniuUtil(){
        //初始化基本配置
        Configuration cfg = new Configuration(Zone.zone0());
        //创建上传管理器
        manager = new UploadManager(cfg);
    }

    public String upload(String imageName,byte [] bytes){
        Auth auth = Auth.create(ACCESSKEY, SECRETKEY);
        //构造覆盖上传token
        String upToken = auth.uploadToken(BUCKETNAME, imageName);
        try {
            Response response = manager.put(bytes, imageName, upToken);
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            //返回请求地址
            return FILEDOMAIN + putRet.key;
        } catch (QiniuException e) {
            e.printStackTrace();
        }
        return null;
    }
}
