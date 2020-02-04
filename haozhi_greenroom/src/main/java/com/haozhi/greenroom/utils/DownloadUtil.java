package com.haozhi.greenroom.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * 下载工具类
 */
public class DownloadUtil {
	
	/**
	 * 通用下载设置
	 * @param response
	 * @param outData 输出数据 ByteArrayOutputStream
	 * @param fileName 文件名
	 * @throws Exception
	 */
	public static void generalDownload(HttpServletResponse response, ByteArrayOutputStream outData, String fileName) throws Exception{
		response.setContentType( "application/x-msdownload");
		response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
		ServletOutputStream out = response.getOutputStream();
		outData.writeTo(out);
		out.flush();
		out.close();
	}
	
	/**
	 * 通用下载设置
	 * @param response 
	 * @param outData 输出数据 InputStream
	 * @param fileName 文件名
	 * @throws Exception
	 */
	public static void generalDownload(HttpServletResponse response, InputStream outData, String fileName) throws Exception{
		response.setContentType( "application/x-msdownload");
		response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
		ServletOutputStream out = response.getOutputStream();
		byte[] bffer = new byte[1024];
		int r = outData.read(bffer, 0, 1024);
		while (r != -1) {
			out.write(bffer);
			r = outData.read(bffer, 0, 1024);
		}
		out.flush();
		out.close();
	}
	
	/**
	 * 通用下载设置
	 * @param response 
	 * @param outData 输出数据 byte[]
	 * @param fileName 文件名
	 * @throws Exception
	 */
	public static void generalDownload(HttpServletResponse response, byte[] outData, String fileName) throws Exception{
		response.setContentType( "application/x-msdownload");
		response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
		ServletOutputStream out = response.getOutputStream();
		out.write(outData);
		out.flush();
		out.close();
	}
}