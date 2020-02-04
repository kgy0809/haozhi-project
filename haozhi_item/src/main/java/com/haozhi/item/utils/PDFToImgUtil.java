package com.haozhi.item.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/10 12:35
 */
public class PDFToImgUtil {
    private static Logger logger = LoggerFactory.getLogger(PDFToImgUtil.class);

    /**
     * 获取PDF总页数
     *
     * @throws IOException
     */
    public static int getPDFNum(String fileUrl) throws IOException {
        PDDocument pdDocument = null;
        int pages = 0;
        try {
            pdDocument = getPDDocument(fileUrl);
            pages = pdDocument.getNumberOfPages();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        } finally {
            if (pdDocument != null) {
                pdDocument.close();
            }
        }
        return pages;
    }

    /**
     * PDF转图片 根据页码一页一页转
     *
     * @throws IOException imgType:转换后的图片类型 jpg,png
     */
    public static String PDFToImg(String name, String fileUrl, int page, String imgType) throws IOException {
        PDDocument pdDocument = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        /* dpi越大转换后越清晰，相对转换速度越慢 */
        int dpi = 100;
        try {
            pdDocument = getPDDocument(fileUrl);
            PDFRenderer renderer = new PDFRenderer(pdDocument);
            QiniuUtil qiniuUtil = new QiniuUtil();
            int pages = pdDocument.getNumberOfPages();
            String upload = null;
            for (int i = 0; i < page; i++) {
                if (page <= pages && page > 0) {
                    BufferedImage image = renderer.renderImageWithDPI(i, dpi);
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    ImageIO.write(image, imgType, byteArrayOutputStream);
                    upload = qiniuUtil.upload(name + "-" + i, byteArrayOutputStream.toByteArray());
                }
            }
            return upload;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        } finally {
            if (pdDocument ==null)
                pdDocument.close();
            byteArrayOutputStream.close();
        }
        return null;
    }

    private static PDDocument getPDDocument(String fileUrl) throws IOException {
        File file = new File(fileUrl);
        FileInputStream inputStream = new FileInputStream(file);
        return PDDocument.load(inputStream);
    }
}
