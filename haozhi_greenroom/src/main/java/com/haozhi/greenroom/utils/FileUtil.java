package com.haozhi.greenroom.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件工具类
 */
public class FileUtil {
	
	/**
	 * MultipartFile 转  File
	 * @param file
	 * 注意：转换的文件保存在项目的根目录，因此使用完要及时清理
	 * @throws Exception
	 */
	public static File multipartFileToFile(MultipartFile file) throws Exception {
	    File toFile = null;
	    if (file != null) {
            InputStream in = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            
            OutputStream os = new FileOutputStream(toFile);
	        int r = 0;
	        byte[] buffer = new byte[1024];
	        while ((r = in.read(buffer, 0, 1024)) != -1) {
	            os.write(buffer, 0, r);
	        }
	        try {
	        	if (os != null) {
	        		os.close();
	        	}
			} catch (Exception e) {
			}
	        try {
	        	if (in != null) {
	        		in.close();
	        	}
			} catch (Exception e) {
			}
	    }
	    return toFile;
	}
	
}
