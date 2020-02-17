package com.haozhi.greenroom.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.haozhi.common.dto.PageResultDTO;
import com.haozhi.greenroom.dao.InvoiceMapper;
import com.haozhi.greenroom.pojo.Account;
import com.haozhi.greenroom.pojo.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/14 15:13
 */
@Controller
@RequestMapping("invoice")
public class InvoiceController {

    @Autowired
    private InvoiceMapper invoiceMapper;

    @RequestMapping(method = RequestMethod.GET)
    public String queryInvoice() {
        return "tgls/system/Invoice/invoice_list";
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public PageResultDTO pageDeposit(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                     @RequestParam(value = "rows", defaultValue = "10") Integer rows,
                                     @RequestParam(value = "uid", required = false) String uid,
                                     @RequestParam(value = "id", required = false) String id,
                                     @RequestParam(value = "time1", required = false) String time1,
                                     @RequestParam(value = "time2", required = false) String time2
    ) {
        if (id != null && !("").equals(id)) {
            Invoice invoice = invoiceMapper.selectByPrimaryKey(id);
            List<Invoice> list = new ArrayList<>();
            if (invoice != null) {
                list.add(invoice);
            }
            return new PageResultDTO((long) list.size(), list);
        }
        if (uid != null && !("").equals(uid)) {
            Example example = new Example(Invoice.class);
            example.setOrderByClause(" time desc");
            example.createCriteria().andEqualTo("userId",uid);
            List<Invoice> invoices = invoiceMapper.selectByExample(example);
            return new PageResultDTO((long) invoices.size(), invoices);
        }
        if ((time1 != null && !("").equals(time1)) || (time2 != null && !("").equals(time2))) {
            List<Invoice> invoices = invoiceMapper.selectByTime1AndTime2(time1,time2);
            return new PageResultDTO((long) invoices.size(), invoices);
        }
        PageHelper.startPage(page, rows);
        Example example = new Example(Invoice.class);
        example.setOrderByClause(" time desc");
        List<Invoice> invoices = invoiceMapper.selectByExample(example);
        PageInfo<Invoice> accountPageInfo = new PageInfo<>(invoices);
        return new PageResultDTO(accountPageInfo.getTotal(), accountPageInfo.getList());
    }

    @RequestMapping("vive")
    public String viveInvoice(String id, Map<String, Object> map) {
        Invoice invoice = invoiceMapper.selectByPrimaryKey(id);
        map.put("invoice", invoice);
        return "tgls/system/Invoice/invoice_vive";
    }

    @RequestMapping("look")
    public String lookInvoice(String id, Map<String, Object> map) {
        Invoice invoice = invoiceMapper.selectByPrimaryKey(id);
        map.put("invoice", invoice);
        return "tgls/system/Invoice/invoice_look";
    }

    @RequestMapping("SFkt")
    public String lookInvoice(String id, String order, Map<String, Object> map) {
        Invoice invoice = new Invoice();
        invoice.setId(id);
        invoice.setState("3");
        invoice.setExpress(order);
        invoiceMapper.updateByPrimaryKeySelective(invoice);
        return "redirect:/invoice";
    }

    @RequestMapping("update")
    public String uodateInvoice(String id) {
        Invoice invoice = new Invoice();
        invoice.setId(id);
        invoice.setState("2");
        invoiceMapper.updateByPrimaryKeySelective(invoice);
        return "redirect:/invoice";
    }
}
