package com.haozhi.item.service;

import com.haozhi.common.utils.IdWorker;
import com.haozhi.item.dao.BusinessTwoMapper;
import com.haozhi.item.dao.HzYwRepository;
import com.haozhi.item.dao.MenuMapper;
import com.haozhi.item.dao.OrderMapper;
import com.haozhi.item.dto.LastDto;
import com.haozhi.item.pojo.*;
import com.haozhi.item.utils.MoneyUtils;
import com.haozhi.item.utils.PDFToImgUtil;
import com.haozhi.item.utils.QiniuUtil;
import com.haozhi.item.utils.WorderToNewWordUtils;
import com.haozhi.item.web.controller.textDemo;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.ToPdfParameterList;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import tk.mybatis.mapper.entity.Example;

import javax.mail.MessagingException;
import java.io.*;
import java.util.*;

import static com.haozhi.item.utils.CreatePdfInvoice.writeDataToDocument;

/**
 * @author kgy
 * @version 1.0
 * @date 2019/12/27 10:21
 */
@Service
public class HzYwService {

    @Autowired
    private HzYwRepository hzYwRepository;

    @Autowired
    private BusinessTwoMapper businessTwoMapper;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private OrderMapper orderMapper;

    /**
     * 查询 one 的数据
     *
     * @param id
     * @return
     */
    public List<HzYw> queryById(String id) {
        Example example = new Example(HzYw.class);
        example.createCriteria().andEqualTo("zid", id);
        return hzYwRepository.selectByExample(example);
    }

    /**
     * 保存two一个个数据
     *
     * @param businessTwo
     */
    public String businessTextTwo(BusinessTwo businessTwo) {
        businessTwo.setId(idWorker.nextId() + "");
        if (businessTwo.getSbImage() == "" || businessTwo.getSbImage() == null) {
            businessTwo.setSbImage(null);
        }
        businessTwoMapper.insert(businessTwo);
        return businessTwo.getId();
    }

    public HzYw queryGYById(String oneId) {
        return hzYwRepository.queryGYById(oneId);
    }

    /**
     * ajax 根据id进行渲染
     *
     * @param id
     * @return
     */
    public List softListById(String id, String sId) {
        Example example = new Example(HzYw.class);
        example.createCriteria().andEqualTo("sjName", sId).andEqualTo("zid", id);
        return hzYwRepository.selectByExample(example);
        /*return hzYwRepository.softListById(id,sId);*/
    }

    /**
     * 根据id查询信息
     *
     * @param id
     * @return
     */
    public HzYw softNumbById(String id, User user) {
        HzYw hzYw = hzYwRepository.selectByPrimaryKey(id);
        BusinessTwo businessTwo = new BusinessTwo();
        businessTwo.setId(idWorker.nextId() + "");
        businessTwo.setOneId(hzYw.getId());
        if ("1".equals(user.getState())) {
            businessTwo.setPrice(hzYw.getGfPrice() + hzYw.getHyPrice());
        } else {
            businessTwo.setPrice(hzYw.getGfPrice() + hzYw.getVipPrice());
        }
        businessTwoMapper.insertSelective(businessTwo);
        hzYw.setId(businessTwo.getId());
        return hzYw;
    }

    /**
     * ajax 修改订单里的数据
     *
     * @param twoId  订单id
     * @param menuId 菜单选择数组
     * @param price  价格
     */
    public void updateThree(String twoId, String menuId, Integer price) {
        BusinessTwo businessTwo = new BusinessTwo();
        businessTwo.setId(twoId);
        businessTwo.setMenuId(menuId);
        businessTwo.setPrice(price);
        businessTwoMapper.updateByPrimaryKeySelective(businessTwo);
    }

    /**
     * @param dataArr 数据
     * @param twoId   key id
     */
    public BusinessTwo updateFour(BusinessTwo dataArr, String twoId) {
        if (dataArr.getCommission() == null || dataArr.getCommission().equals("")) {
            dataArr.setCommission(0 * 100);
        }
        if (dataArr.getNumber() == null) {
            dataArr.setNumber(1);
        }
        dataArr.setCommission(dataArr.getCommission() * 100);
        dataArr.setId(twoId);
        dataArr.setTime(new Date());
        businessTwoMapper.updateByPrimaryKeySelective(dataArr);
        return dataArr;
    }

    /**
     * 国际保存一级菜单的id
     *
     * @param id
     */
    public void updateGgTwo(String id, String towId) {
        BusinessTwo businessTwo = new BusinessTwo();
        businessTwo.setId(towId);
        businessTwo.setMenuId(id);
        businessTwoMapper.updateByPrimaryKeySelective(businessTwo);
    }

    /**
     * 下载word委托书
     *
     * @param twoId
     */
    public String downloadWord(String twoId) {
        BusinessTwo businessTwo = businessTwoMapper.selectByPrimaryKey(twoId);
        String oneId = businessTwo.getOneId();
        HzYw hzYw = hzYwRepository.selectByPrimaryKey(oneId);
        return hzYw.getWordUrl();
    }

    /**
     * 支付确定页信息查询
     *
     * @param twoId
     * @return
     */
    public LastDto businessLast(String twoId, User user) {
        BusinessTwo businessTwo = businessTwoMapper.selectByPrimaryKey(twoId);
        LastDto lastDto = new LastDto();
        BeanUtils.copyProperties(businessTwo, lastDto);
        String oneId = businessTwo.getOneId();
        HzYw hzYw = hzYwRepository.selectByPrimaryKey(oneId);
        lastDto.setSqrMessage(hzYw.getName());
        if (businessTwo.getPrice() == null || businessTwo.getPrice().equals("")) {
            if (user.getState().equals("1")) {
                businessTwo.setPrice(hzYw.getHyPrice());
            } else if (user.getState().equals("2")) {
                businessTwo.setPrice(hzYw.getVipPrice());
            }
        }
        lastDto.setPrice(businessTwo.getPrice() + businessTwo.getCommission());
        String[] split = businessTwo.getMenuId().split(",");
        if (split.length > 0) {
            Menu key = menuMapper.selectByPrimaryKey(split[0]);
            Menu key1 = menuMapper.selectByPrimaryKey(key.getPid());
            if (key1 != null) {
                Menu key2 = menuMapper.selectByPrimaryKey(key1.getPid());
                lastDto.setOneName(key2.getName());
            }
        }
        List<Menu> list = new ArrayList<>();
        for (String s : split) {
            Menu menu = menuMapper.selectByPrimaryKey(s);
            list.add(menu);
        }
        lastDto.setMenuName(list);
        return lastDto;
    }

    public BusinessTwo etc(String twoId) {
        return businessTwoMapper.selectByPrimaryKey(twoId);
    }

    public void updateEtc(String twoId, BusinessTwo businessTwo) {
        if (twoId != null) {
            businessTwo.setId(twoId);
        }
        businessTwoMapper.updateByPrimaryKeySelective(businessTwo);
    }

    public HzYw softById(String id) {
        return hzYwRepository.selectByPrimaryKey(id);
    }

    public LastDto updateLast(String softId) {
        BusinessTwo businessTwo = businessTwoMapper.selectByPrimaryKey(softId);
        LastDto lastDto = new LastDto();
        BeanUtils.copyProperties(businessTwo, lastDto);
        String oneId = businessTwo.getOneId();
        HzYw hzYw = hzYwRepository.selectByPrimaryKey(oneId);
        lastDto.setSqrMessage(hzYw.getName());
        lastDto.setApplication(hzYw.getSjName());
        if (businessTwo.getPrice() == null || businessTwo.getPrice().equals("")) {
            businessTwo.setPrice(0);
        }
        lastDto.setPrice(businessTwo.getPrice() * businessTwo.getNumber() + businessTwo.getCommission());
        return lastDto;
    }

    @Autowired
    private MailService mailService;

    private static final String SUBJECT = "好智企业";
    private static final String CONTNET = "好智企业发送";

    /**
     * 写入 word数据 转换成 pdf
     */
    public String downloadContract(String twoId, User user, String type, String email, String id) throws MessagingException, IOException {
        BusinessTwo business = null;
        if (id != null && !("").equals(id)) {
            business = businessTwoMapper.selectByPrimaryKey(id);
        } else {
            business = businessTwoMapper.selectByPrimaryKey(twoId);
        }
        Example example = new Example(Order.class);
        example.createCriteria().andEqualTo("pOrder", business.getId());
        Order order = orderMapper.selectOneByExample(example);
        /**
         * 逐一国
         */
        if ("10014".equals(business.getOneId())) {
            Document doc = new Document();
            doc.loadFromFile("/www/server/haozhi/word/008.docx");

            if ("1".equals(business.getApplication())) {
                doc.replace("#qgName", "执照代码", true, true);
            } else if ("2".equals(business.getApplication())) {
                doc.replace("#qgName", "身份证号", true, true);
            }

            //替换文档中以#开头的文本
            doc.replace("#jfGsName", business.getApplicationName(), true, true);
            doc.replace("#jfName", business.getApplicationNumName(), true, true);
            doc.replace("#jfEmail", business.getApplicationNumMail(), true, true);
            doc.replace("#jfNum", business.getApplicationNumTel(), true, true);
            if (order != null) {
                doc.replace("#stime", order.getSTime(), true, true);
            } else {
                doc.replace("#stime", business.getSTime(), true, true);
            }
            doc.replace("#jfXxdm", business.getApplicationId(), true, true);
            doc.replace("#gwName", user.getName(), true, true);
            doc.replace("#gwNum", user.getTel(), true, true);

            HzYw hzYw = hzYwRepository.selectByPrimaryKey(business.getOneId());
            if (user.getState().equals("1")) {
                doc.replace("#dlPrice", String.valueOf(hzYw.getHyPrice() / 100), true, true);
                doc.replace("#dxZjPrice", MoneyUtils.change(hzYw.getHyPrice() / 100), true, true);
            } else if (user.getState().equals("2")) {
                doc.replace("#dlPrice", String.valueOf(hzYw.getVipPrice() / 100 + business.getCommission() / 100), true, true);
                doc.replace("#dxZjPrice", MoneyUtils.change(hzYw.getVipPrice() / 100 + business.getCommission() / 100), true, true);
            }

            //更新域
            doc.isUpdateFields(true);
            String name = idWorker.nextId() + "";
            //保存为PDF格式文档
            doc.saveToFile("/www/server/haozhi/pdf/" + name + ".pdf", FileFormat.PDF);
            File file = new File("/www/server/haozhi/pdf/" + name + ".pdf");

            if ("1".equals(type)) {
                QiniuUtil qiniuUtil = new QiniuUtil();
                String invoice = qiniuUtil.upload(name, file);
                file.delete();
                doc.close();
                return invoice;
            } else if ("2".equals(type)) {
                mailService.sendAttachmentsMail(email, SUBJECT, CONTNET, "/www/server/haozhi/pdf/" + name + ".pdf");
                file.delete();
                doc.close();
                return "email发送成功";
            }
            PDFToImgUtil pdfToImgUtil = new PDFToImgUtil();
            int pdfNum = pdfToImgUtil.getPDFNum("/www/server/haozhi/pdf" + name + ".pdf");
            String png = pdfToImgUtil.PDFToImg(name, "/www/server/haozhi/pdf" + name + ".pdf", pdfNum, "png");
            file.delete();
            doc.close();
            return png;
        } else if ("10015".equals(business.getOneId())) {
            /**
             * 马德里
             */
            Document doc = new Document();
            doc.loadFromFile("/www/server/haozhi/word/009.docx");

            if ("1".equals(business.getApplication())) {
                doc.replace("#qgName", "执照代码", true, true);
            } else if ("2".equals(business.getApplication())) {
                doc.replace("#qgName", "身份证号", true, true);
            }

            //替换文档中以#开头的文本
            doc.replace("#jfGsName", business.getApplicationName(), true, true);
            doc.replace("#jfName", business.getApplicationNumName(), true, true);
            doc.replace("#jfEmail", business.getApplicationNumMail(), true, true);
            doc.replace("#jfNum", business.getApplicationNumTel(), true, true);
            doc.replace("#jfXxdm", business.getApplicationId(), true, true);
            doc.replace("#gwName", user.getName(), true, true);
            doc.replace("#gwNum", user.getTel(), true, true);
            if (order != null) {
                doc.replace("#stime", order.getSTime(), true, true);
            } else {
                doc.replace("#stime", business.getSTime(), true, true);
            }
            doc.replace("#zjPrice", String.valueOf(business.getPrice() / 100 + business.getCommission() / 100), true, true);
            doc.replace("#dxZjPrice", MoneyUtils.change(Double.parseDouble(String.valueOf(business.getPrice() / 100 + business.getCommission() / 100))), true, true);
            HzYw hzYw = hzYwRepository.selectByPrimaryKey(business.getOneId());
            if (user.getState().equals("1")) {
                doc.replace("#dlPrice", String.valueOf(hzYw.getHyPrice() / 100), true, true);
            } else if (user.getState().equals("2")) {
                doc.replace("#dlPrice", String.valueOf(hzYw.getVipPrice() / 100 + business.getCommission() / 100), true, true);
            }

            //更新域
            doc.isUpdateFields(true);
            String name = idWorker.nextId() + "";
            //保存为PDF格式文档
            doc.saveToFile("/www/server/haozhi/pdf/" + name + ".pdf", FileFormat.PDF);
            File file = new File("/www/server/haozhi/pdf/" + name + ".pdf");

            if ("1".equals(type)) {
                QiniuUtil qiniuUtil = new QiniuUtil();
                String invoice = qiniuUtil.upload(name, file);
                file.delete();
                doc.close();
                return invoice;
            } else if ("2".equals(type)) {
                mailService.sendAttachmentsMail(email, SUBJECT, CONTNET, "/www/server/haozhi/pdf/" + name + ".pdf");
                file.delete();
                doc.close();
                return "email发送成功";
            }
            PDFToImgUtil pdfToImgUtil = new PDFToImgUtil();
            int pdfNum = pdfToImgUtil.getPDFNum("/www/server/haozhi/pdf/" + name + ".pdf");
            String png = pdfToImgUtil.PDFToImg(name, "/www/server/haozhi/pdf/" + name + ".pdf", pdfNum, "png");
            file.delete();
            doc.close();
            return png;
        } else {

            //加载Word模板文档
            if ("10013".equals(business.getOneId()))
                return null;
            Document doc = new Document();
            doc.loadFromFile("/www/server/haozhi/word/001.docx");
            if ("1".equals(business.getApplication())) {
                doc.replace("#qgName", "执照代码", true, true);
            } else if ("2".equals(business.getApplication())) {
                doc.replace("#qgName", "身份证号", true, true);
            }
            //替换文档中以#开头的文本
            doc.replace("#jfGsName", business.getApplicationName(), true, true);
            doc.replace("#jfName", business.getApplicationNumName(), true, true);
            doc.replace("#jfEmail", business.getApplicationNumMail(), true, true);
            doc.replace("#jfNum", business.getApplicationNumTel(), true, true);
            doc.replace("#jfXxdm", business.getApplicationId(), true, true);
            doc.replace("#gwName", user.getName(), true, true);
            doc.replace("#gwNum", user.getTel(), true, true);
            doc.replace("#sbName", business.getSbName(), true, true);
            String s1 = business.getMenuId().split(",")[0];
            if (order != null) {
                doc.replace("#stime", order.getSTime(), true, true);
            } else {
                doc.replace("#stime", business.getSTime(), true, true);
            }
            Integer pid = menuMapper.selectByPrimaryKey(menuMapper.selectByPrimaryKey(s1).getPid()).getPid();
            doc.replace("#sb1", String.valueOf(pid), true, true);
            doc.replace("#zjPrice", String.valueOf(business.getPrice() / 100 + business.getCommission() / 100), true, true);
            doc.replace("#dxZjPrice", MoneyUtils.change(Double.parseDouble(String.valueOf(business.getPrice() / 100 + business.getCommission() / 100))), true, true);
            HzYw hzYw = hzYwRepository.selectByPrimaryKey(business.getOneId());
            doc.replace("#Name", hzYw.getName(), true, true);
            doc.replace("#gfPrice", String.valueOf((hzYw.getGfPrice() / 100)), true, true);
            if (user.getState().equals("1")) {
                doc.replace("#dlPrice", String.valueOf(hzYw.getHyPrice() / 100), true, true);
            } else if (user.getState().equals("2")) {
                doc.replace("#dlPrice", String.valueOf(hzYw.getVipPrice() / 100 + business.getCommission() / 100), true, true);
            }
            String[] split = business.getMenuId().split(",");
            List<WordMune> list = new LinkedList();
            String oneName = null;
            String twoName = null;
            for (String sId : split) {
                WordMune wordMune = new WordMune();
                Menu three = menuMapper.selectByPrimaryKey(sId);
                Menu two = menuMapper.selectByPrimaryKey(three.getPid());
                Menu one = menuMapper.selectByPrimaryKey(two.getPid());
                wordMune.setOne(one.getName());
                wordMune.setTwo(String.valueOf(two.getName().split(" ")[0]));
                wordMune.setThree(three.getName());
                if (one.getName().equals(oneName)) {
                    wordMune.setOne("");
                }
                if (two.getName().equals(twoName)) {
                    wordMune.setTwo("");
                }
                oneName = one.getName();
                twoName = two.getName();
                list.add(wordMune);
            }

            //定义客户购买数据
            String[][] purchaseData = new String[list.size()][3];
            for (int i = 0; i < list.size(); i++) {
                WordMune t = list.get(i);
                purchaseData[i][0] = t.getOne();
                purchaseData[i][1] = t.getTwo();
                purchaseData[i][2] = t.getThree();
            }

            //将购买数据写入模板文档的第二个表格
            writeDataToDocument(doc, purchaseData);
            //更新域
            doc.isUpdateFields(true);
            String name = idWorker.nextId() + "";
            //保存为PDF格式文档
            doc.saveToFile("/www/server/haozhi/pdf/" + name + ".pdf", FileFormat.PDF);
            File file = new File("/www/server/haozhi/pdf/" + name + ".pdf");
            if ("1".equals(type)) {
                QiniuUtil qiniuUtil = new QiniuUtil();
                String invoice = qiniuUtil.upload(name, file);
                doc.close();
                file.delete();
                return invoice;
            } else if ("2".equals(type)) {
                mailService.sendAttachmentsMail(email, SUBJECT, CONTNET, "/www/server/haozhi/pdf/" + name + ".pdf");
                doc.close();
                file.delete();
                return "email发送成功";
            } else {
                PDFToImgUtil pdfToImgUtil = new PDFToImgUtil();
                int pdfNum = pdfToImgUtil.getPDFNum(file.getPath());
                String png = pdfToImgUtil.PDFToImg(name, file.getPath(), pdfNum, "png");
                doc.close();
                file.delete();
                return png;
            }
        }
    }

    /**
     * flow 商标转让 合同下载
     *
     * @param flowId
     * @param user
     * @param type
     * @param email
     * @return
     */
    public String flowDownloadContract(String flowId, User user, String type, String email, String id) throws IOException, MessagingException {
        BusinessTwo business = null;
        if (id != null && !("").equals(id)) {
            business = businessTwoMapper.selectByPrimaryKey(id);
        } else {
            business = businessTwoMapper.selectByPrimaryKey(flowId);
        }
        Example example = new Example(Order.class);
        example.createCriteria().andEqualTo("pOrder", business.getId());
        Order order = orderMapper.selectOneByExample(example);
        Document doc = new Document();
        doc.loadFromFile("/www/server/haozhi/word/004.docx");

        //替换文档中以#开头的文本
        doc.replace("#jfGsName", business.getApplicationNumName(), true, true);
        doc.replace("#jfName", business.getApplicationNumName(), true, true);
        doc.replace("#jfEmail", business.getApplicationNumMail(), true, true);
        doc.replace("#jfNum", business.getApplicationNumTel(), true, true);
        doc.replace("#gwName", user.getName(), true, true);
        doc.replace("#gwNum", user.getTel(), true, true);
        HzYw hzYw = hzYwRepository.selectByPrimaryKey(business.getOneId());
        doc.replace("#Name", hzYw.getName(), true, true);
        doc.replace("#sbNum", business.getZchVal(), true, true);
        if (order != null) {
            doc.replace("#stime", order.getSTime(), true, true);
        } else {
            doc.replace("#stime", business.getSTime(), true, true);
        }
        doc.replace("#lb", business.getLbVal(), true, true);
        doc.replace("#gfPrice", String.valueOf(hzYw.getGfPrice() / 100), true, true);

        doc.replace("#zjPrice", String.valueOf(business.getPrice() / 100 + business.getCommission() / 100), true, true);
        doc.replace("#dxZjPrice", MoneyUtils.change(Double.parseDouble(String.valueOf(business.getPrice() / 100 + business.getCommission() / 100))), true, true);
        if (user.getState().equals("1")) {
            doc.replace("#dlPrice", String.valueOf(hzYw.getHyPrice() / 100), true, true);
        } else if (user.getState().equals("2")) {
            doc.replace("#dlPrice", String.valueOf(hzYw.getVipPrice() / 100 + business.getCommission()), true, true);
        }

        //更新域
        doc.isUpdateFields(true);
        String name = idWorker.nextId() + "";
        //保存为PDF格式文档
        doc.saveToFile("/www/server/haozhi/pdf/" + name + ".pdf", FileFormat.PDF);
        File file = new File("/www/server/haozhi/pdf/" + name + ".pdf");

        if ("1".equals(type)) {
            QiniuUtil qiniuUtil = new QiniuUtil();
            String invoice = qiniuUtil.upload(name, file);
            file.delete();
            doc.close();
            return invoice;
        } else if ("2".equals(type)) {
            mailService.sendAttachmentsMail(email, SUBJECT, CONTNET, "/www/server/haozhi/pdf/" + name + ".pdf");
            file.delete();
            doc.close();
            return "email发送成功";
        }
        PDFToImgUtil pdfToImgUtil = new PDFToImgUtil();
        int pdfNum = pdfToImgUtil.getPDFNum("/www/server/haozhi/pdf/" + name + ".pdf");
        String png = pdfToImgUtil.PDFToImg(name, "/www/server/haozhi/pdf/" + name + ".pdf", pdfNum, "png");
        file.delete();
        doc.close();
        return png;
    }

    /**
     * case 商标案件服务 合同下载
     *
     * @param caseId
     * @param user
     * @param type
     * @param email
     * @return
     */
    public String caseDownloadContract(String caseId, User user, String type, String email, String id) throws MessagingException, IOException {
        BusinessTwo business = null;
        if (id != null && !("").equals(id)) {
            business = businessTwoMapper.selectByPrimaryKey(id);
        } else {
            business = businessTwoMapper.selectByPrimaryKey(caseId);
        }
        Example example = new Example(Order.class);
        example.createCriteria().andEqualTo("pOrder", business.getId());
        Order order = orderMapper.selectOneByExample(example);
        Document doc = new Document();
        doc.loadFromFile("/www/server/haozhi/word/005.docx");

        //替换文档中以#开头的文本
        doc.replace("#jfGsName", business.getApplicationNumName(), true, true);
        doc.replace("#jfName", business.getApplicationNumName(), true, true);
        doc.replace("#jfEmail", business.getApplicationNumMail(), true, true);
        doc.replace("#jfNum", business.getApplicationNumTel(), true, true);
        doc.replace("#gwName", user.getName(), true, true);
        doc.replace("#gwNum", user.getTel(), true, true);

        HzYw hzYw = hzYwRepository.selectByPrimaryKey(business.getOneId());
        doc.replace("#Name", hzYw.getName(), true, true);
        doc.replace("#sbNum", business.getZchVal(), true, true);
        if (order != null) {
            doc.replace("#stime", order.getSTime(), true, true);
        } else {
            doc.replace("#stime", business.getSTime(), true, true);
        }
        doc.replace("#lb", business.getLbVal(), true, true);
        doc.replace("#abcName", business.getApplicationName(), true, true);
        doc.replace("#gfPrice", String.valueOf(hzYw.getGfPrice() / 100), true, true);

        doc.replace("#zjPrice", String.valueOf(business.getPrice() / 100 + business.getCommission() / 100), true, true);
        doc.replace("#dxZjPrice", MoneyUtils.change(Double.parseDouble(String.valueOf(business.getPrice() / 100 + business.getCommission() / 100))), true, true);
        if (user.getState().equals("1")) {
            doc.replace("#dlPrice", String.valueOf(hzYw.getHyPrice() / 100), true, true);
        } else if (user.getState().equals("2")) {
            doc.replace("#dlPrice", String.valueOf(hzYw.getVipPrice() / 100 + business.getCommission() / 100), true, true);
        }

        //更新域
        doc.isUpdateFields(true);
        String name = idWorker.nextId() + "";
        //保存为PDF格式文档
        doc.saveToFile("/www/server/haozhi/pdf/" + name + ".pdf", FileFormat.PDF);
        doc.close();
        File file = new File("/www/server/haozhi/pdf/" + name + ".pdf");

        if ("1".equals(type)) {
            QiniuUtil qiniuUtil = new QiniuUtil();
            String invoice = qiniuUtil.upload(name, file);
            file.delete();
            return invoice;
        } else if ("2".equals(type)) {
            mailService.sendAttachmentsMail(email, SUBJECT, CONTNET, "/www/server/haozhi/pdf/" + name + ".pdf");
            file.delete();
            return "email发送成功";
        } else {
            PDFToImgUtil pdfToImgUtil = new PDFToImgUtil();
            int pdfNum = pdfToImgUtil.getPDFNum("/www/server/haozhi/pdf/" + name + ".pdf");
            String png = pdfToImgUtil.PDFToImg(name, "/www/server/haozhi/pdf/" + name + ".pdf", pdfNum, "png");
            file.delete();
            return png;
        }
    }

    /**
     * 软著
     *
     * @param softId
     * @param user
     * @param type
     * @param email
     * @return
     * @throws MessagingException
     * @throws IOException
     */
    public String softDownloadContract(String softId, User user, String type, String email, String id) throws MessagingException, IOException {
        BusinessTwo business = null;
        if (id != null && !("").equals(id)) {
            business = businessTwoMapper.selectByPrimaryKey(id);
        } else {
            business = businessTwoMapper.selectByPrimaryKey(softId);
        }
        Example example = new Example(Order.class);
        example.createCriteria().andEqualTo("pOrder", business.getId());
        Order order = orderMapper.selectOneByExample(example);

        Document doc = new Document();
        doc.loadFromFile("/www/server/haozhi/word/006.docx");

        //替换文档中以#开头的文本
        doc.replace("#name", business.getApplicationNumName(), true, true);
        doc.replace("#jfXxdm", business.getApplicationNumName(), true, true);
        doc.replace("#jfName", business.getApplicationNumName(), true, true);
        doc.replace("#Email", business.getApplicationNumMail(), true, true);
        doc.replace("#jfNum", business.getApplicationNumTel(), true, true);
        doc.replace("#gwName", user.getName(), true, true);
        doc.replace("#gwNum", user.getTel(), true, true);
        if (order != null) {
            doc.replace("#stime", order.getSTime(), true, true);
        } else {
            doc.replace("#stime", business.getSTime(), true, true);
        }
        doc.replace("#num", String.valueOf(business.getNumber()), true, true);
        HzYw hzYw = hzYwRepository.selectByPrimaryKey(business.getOneId());
        doc.replace("#sb1", hzYw.getName(), true, true);
        doc.replace("#sbName", business.getSTime(), true, true);
        if (hzYw.getSjName().equals("1")) {
            String abcName = "软著代理";
            doc.replace("#abcName", abcName, true, true);
        } else if (hzYw.getSjName().equals("2")) {
            String abcName = "软著撰写+代理";
            doc.replace("#abcName", abcName, true, true);
        }
        doc.replace("#gfPrice", String.valueOf(hzYw.getGfPrice() / 100), true, true);

        doc.replace("#zjPrice", String.valueOf(business.getPrice() * business.getNumber() / 100 + business.getCommission() / 100), true, true);
        doc.replace("#dxZjPrice", MoneyUtils.change(Double.parseDouble(String.valueOf(business.getPrice() * business.getNumber() / 100 + business.getCommission() / 100))), true, true);
        if (user.getState().equals("1")) {
            doc.replace("#dlPrice", String.valueOf(hzYw.getHyPrice() / 100), true, true);
        } else if (user.getState().equals("2")) {
            doc.replace("#dlPrice", String.valueOf(hzYw.getVipPrice() / 100 + business.getCommission() / 100), true, true);
        }
        //更新域
        doc.isUpdateFields(true);
        String name = idWorker.nextId() + "";
        //保存为PDF格式文档
        doc.saveToFile("/www/server/haozhi/pdf/" + name + ".pdf",FileFormat.PDF);
        doc.close();

        File file = new File("/www/server/haozhi/pdf/"+name+".pdf");

        if ("1".equals(type)) {
            QiniuUtil qiniuUtil = new QiniuUtil();
            String invoice = qiniuUtil.upload(name, file);
            doc.close();
            file.delete();
            return invoice;
        } else if ("2".equals(type)) {
            mailService.sendAttachmentsMail(email, SUBJECT, CONTNET, "/www/server/haozhi/pdf/" + name + ".pdf");
            doc.close();
            file.delete();
            return "email发送成功";
        }
        PDFToImgUtil pdfToImgUtil = new PDFToImgUtil();
        int pdfNum = pdfToImgUtil.getPDFNum("/www/server/haozhi/pdf/"+name+".pdf");
        String png = pdfToImgUtil.PDFToImg(name, "/www/server/haozhi/pdf/"+name+".pdf", pdfNum, "png");
        file.delete();
        return png;
    }

    /**
     * 代理
     *
     * @param copyrightId
     * @param user
     * @param type
     * @param email
     * @return
     * @throws MessagingException
     * @throws IOException
     */
    public String copyrightDownloadContract(String copyrightId, User user, String type, String email, String id) throws MessagingException, IOException {
        BusinessTwo business = null;
        if (id != null && !("").equals(id)) {
            business = businessTwoMapper.selectByPrimaryKey(id);
        } else {
            business = businessTwoMapper.selectByPrimaryKey(copyrightId);
        }
        Example example = new Example(Order.class);
        example.createCriteria().andEqualTo("pOrder", business.getId());
        Order order = orderMapper.selectOneByExample(example);

        Document doc = new Document();
        doc.loadFromFile("/www/server/haozhi/word/007.docx");

        //替换文档中以#开头的文本
        doc.replace("#jfGsName", business.getApplicationNumName(), true, true);
        doc.replace("#jfXxdm", business.getApplicationNumName(), true, true);
        doc.replace("#jfName", business.getApplicationNumName(), true, true);
        doc.replace("#jfEmail", business.getApplicationNumMail(), true, true);
        doc.replace("#jfNum", business.getApplicationNumTel(), true, true);
        doc.replace("#gwName", user.getName(), true, true);
        if (order != null) {
            doc.replace("#stime", order.getSTime(), true, true);
        } else {
            doc.replace("#stime", business.getSTime(), true, true);
        }
        doc.replace("#num", String.valueOf(business.getNumber()), true, true);
        doc.replace("#gwNum", user.getTel(), true, true);
        HzYw hzYw = hzYwRepository.selectByPrimaryKey(business.getOneId());
        doc.replace("#Name", hzYw.getName(), true, true);
        doc.replace("#zjPrice", String.valueOf(business.getPrice() / 100 + business.getCommission() / 100), true, true);
        /*doc.replace("#dxZjPrice", MoneyUtils.change(Double.parseDouble(String.valueOf(business.getPrice() / 100 + business.getCommission() / 100))), true, true);*/
        doc.replace("#gfPrice", String.valueOf(hzYw.getGfPrice() / 100), true, true);
        if (user.getState().equals("1")) {
            doc.replace("#dlPrice", String.valueOf(hzYw.getHyPrice() / 100), true, true);
        } else if (user.getState().equals("2")) {
            doc.replace("#dlPrice", String.valueOf(hzYw.getVipPrice() / 100 + business.getCommission() / 100), true, true);
        }

        //更新域
        doc.isUpdateFields(true);
        String name = idWorker.nextId() + "";
        //保存为PDF格式文档
        doc.saveToFile("/www/server/haozhi/pdf/" + name + ".pdf", FileFormat.PDF);
        doc.close();
        File file = new File("/www/server/haozhi/pdf/" + name + ".pdf");

        if ("1".equals(type)) {
            QiniuUtil qiniuUtil = new QiniuUtil();
            String invoice = qiniuUtil.upload(name, file);
            doc.close();
            file.delete();
            return invoice;
        } else if ("2".equals(type)) {
            mailService.sendAttachmentsMail(email, SUBJECT, CONTNET, "/www/server/haozhi/pdf/" + name + ".pdf");
            doc.close();
            file.delete();
            return "email发送成功";
        }
        PDFToImgUtil pdfToImgUtil = new PDFToImgUtil();
        int pdfNum = pdfToImgUtil.getPDFNum("/www/server/haozhi/pdf/" + name + ".pdf");
        String png = pdfToImgUtil.PDFToImg(name, "/www/server/haozhi/pdf/" + name + ".pdf", pdfNum, "png");
        file.delete();
        return png;
    }

    public static String getStreamString(InputStream tInputStream) {
        if (tInputStream != null) {

            try {

                BufferedReader tBufferedReader = new BufferedReader(new InputStreamReader(tInputStream));

                StringBuffer tStringBuffer = new StringBuffer();

                String sTempOneLine = new String("");

                while ((sTempOneLine = tBufferedReader.readLine()) != null) {

                    tStringBuffer.append(sTempOneLine);

                }

                return tStringBuffer.toString();

            } catch (Exception ex) {

                ex.printStackTrace();

            }

        }
        return null;
    }
}

