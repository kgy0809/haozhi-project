package com.haozhi.greenroom.controller;

import com.haozhi.greenroom.dao.*;
import com.haozhi.greenroom.pojo.*;
import com.haozhi.greenroom.utils.DateUtil;
import com.haozhi.greenroom.utils.DownloadUtil;
import com.haozhi.greenroom.utils.ExcelDataTest;
import com.haozhi.greenroom.utils.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.haozhi.greenroom.utils.DateUtil.DATE_FORMAT_YYYYMMDD;
import static com.haozhi.greenroom.utils.DateUtil.DATE_FORMAT_YYYY_MMMM_DD;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/15 18:10
 */
@Controller
@RequestMapping("/excel")
public class ExcelController {

    @Autowired
    private HzUserMapper hzUserMapper;
    @Autowired
    private VipTimeMapper vipTimeMapper;
    @Autowired
    private BankMapper bankMapper;
    @Autowired
    private OrderMapper ordermapper;
    @Autowired
    private BusinessTwoMapper businessTwoMapper;
    @Autowired
    private HzZcMapper hzZcMapper;
    @Autowired
    private HzYwMapper hzYwMapper;

    /**
     * 导出Excel数据 根据传过来的 时间对order进行导出
     *
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/export", method = RequestMethod.POST, produces = "application/json")
    public void export(HttpServletResponse response, String time) throws Exception {
        ByteArrayOutputStream baos = null;
        OutputStream out = null;
        List<Order> orders = null;

        if (time != null) {
            Example example = new Example(Order.class);
            example.createCriteria().andEqualTo("state", 3).andLike("time", "%" + time + "%");
            orders = ordermapper.selectByExample(example);
        }

        try {

            AtomicReference<Integer> count = new AtomicReference<>(1);
            // excel数据  实际运用从数据库中查询
            List<ExcelDataTest> dataList = new ArrayList<>();
            if (orders.size() != 0) {

                orders.forEach(order -> {

                    ExcelDataTest data = new ExcelDataTest();
                    HzUser user = hzUserMapper.selectByPrimaryKey(order.getUserId());
                    data.setId(String.valueOf(count));
                    data.setHyId(user.getId());
                    data.setZcTime(DateUtil.parseDateToStr(user.getTime(), DATE_FORMAT_YYYY_MMMM_DD));
                    if (user.getState().equals("2")) {
                        data.setHyTime(DateUtil.parseDateToStr(vipTimeMapper.selectByPrimaryKey(user.getVipTimeId()).getExpireTime(), DATE_FORMAT_YYYY_MMMM_DD));
                    } else {
                        data.setHyTime(" ");
                    }
                    data.setHyName(user.getName());
                    data.setHyTel(user.getTel());
                    Example example = new Example(Bank.class);
                    example.createCriteria().andEqualTo("userId", user.getId());
                    Bank bank = bankMapper.selectOneByExample(example);
                    if (bank != null) {
                        data.setHyBankId(bank.getBankId());
                    } else {
                        data.setHyBankId(" ");
                    }
                    Example countEx = new Example(HzUser.class);
                    countEx.createCriteria().andEqualTo("superId", user.getId());
                    List<HzUser> oneNum = hzUserMapper.selectByExample(countEx);
                    if (oneNum.size() != 0) {
                        data.setOneHyNum(String.valueOf(oneNum.size()));
                        oneNum.forEach(two -> {
                            Example countExTwo = new Example(HzUser.class);
                            countExTwo.createCriteria().andEqualTo("superId", two.getId());
                            List<HzUser> countTwos = hzUserMapper.selectByExample(countExTwo);
                            if (countTwos.size() != 0) {
                                data.setTwoHyNum(String.valueOf(countTwos.size()));
                            } else {
                                data.setTwoHyNum("0");
                            }
                        });
                    } else {
                        data.setOneHyNum("0");
                        data.setTwoHyNum("0");
                    }
                    BusinessTwo businessTwo = businessTwoMapper.selectByPrimaryKey(order.getPOrder());
                    HzYw hzYw = hzYwMapper.selectByPrimaryKey(businessTwo.getOneId());
                    HzZc hzZc = hzZcMapper.selectByPrimaryKey(hzYw.getZid());
                    data.setHtCoding(hzZc.getCoding() + DateUtil.parseDateToStr(order.getTime(), DATE_FORMAT_YYYYMMDD) + "-" + count.getAndSet(count.get() + 1));
                    data.setAahtClasses(hzZc.getName());
                    data.setAaywName(hzYw.getName());
                    data.setNum(String.valueOf(businessTwo.getNumber()));
                    data.setQySum(DateUtil.parseDateToStr(businessTwo.getTime(), DATE_FORMAT_YYYY_MMMM_DD));

                    data.setHtDlPrice(String.valueOf(order.getFwPrice() / 100));
                    data.setGfPrice(String.valueOf(order.getGfPrice() / 100));
                    data.setZPrice(String.valueOf(order.getPrice() / 100));
                    //代理费低价
                    if (user.getState().equals("2")) {
                        data.setVipPrice(String.valueOf(hzYw.getVipPrice() / 100));
                    } else {
                        data.setVipPrice(" ");
                    }
                    data.setFkTime(DateUtil.parseDateToStr(order.getTime(), DATE_FORMAT_YYYY_MMMM_DD));
                    if (order.getInvoiceState().equals("1")) {
                        data.setInvoice("否");
                        data.setInvoiceTime(" ");
                    } else if (order.getInvoiceState().equals("2")) {
                        data.setInvoice("是");
                        data.setInvoiceTime(DateUtil.parseDateToStr(order.getInvoiceTime(), DATE_FORMAT_YYYY_MMMM_DD));
                    }
                    if (user.getState().equals("2")) {
                        if (businessTwo.getCommission() != null) {
                            data.setGwCommission(String.valueOf((businessTwo.getCommission() / 100) * 0.93));
                        } else {
                            data.setGwCommission(" ");
                        }
                    } else {
                        data.setGwCommission(" ");
                    }
                    data.setOneTc(String.valueOf((hzYw.getVipPrice() / 100) * 0.93 * 0.25));
                    data.setTwoTc(String.valueOf((hzYw.getVipPrice() / 100) * 0.93 * 0.05));
                    if (("1").equals(businessTwo.getApplication())) {
                        if (businessTwo.getApplicationName() != null) {
                            data.setHtOwnerName(businessTwo.getApplicationName());
                        } else {
                            data.setHtOwnerName(" ");
                        }
                    } else {
                        data.setHtOwnerName(" ");
                    }
                    data.setHtOwenr(businessTwo.getApplicationNumName());
                    data.setHtOwenrTel(businessTwo.getApplicationNumTel());
                    data.setOwenrEmail(businessTwo.getApplicationNumMail());
                    if (order.getRemark() != null) {
                        data.setRemark(order.getRemark());
                    } else {
                        data.setRemark(" ");
                    }
                    dataList.add(data);
                });
            }
            baos = ExcelUtil.exportExcel(dataList, ExcelDataTest.class, "excel测试模板.xlsx", 1);
            DownloadUtil.generalDownload(response, baos, "excle导出(" + time + ").xlsx");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("导出失败：" + e.getMessage());
        } finally {
            if (baos != null) {
                baos.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

}
