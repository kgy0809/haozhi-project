package com.haozhi.greenroom.controller;

import com.haozhi.common.dto.PageResultDTO;
import com.haozhi.greenroom.dao.InvoiceMapper;
import com.haozhi.greenroom.pojo.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import tk.mybatis.mapper.entity.Example;

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

    @RequestMapping
    public String queryInvoice(Map<String, Object> map) {
        Example example = new Example(Invoice.class);
        example.setOrderByClause(" state asc");
        List<Invoice> invoices = invoiceMapper.selectByExample(example);
        map.put("invoice",invoices);
        return "tgls/system/Invoice/invoice_list";
    }

    @RequestMapping("vive")
    public String viveInvoice(String id,Map<String, Object> map) {
        Invoice invoice = invoiceMapper.selectByPrimaryKey(id);
        map.put("invoice",invoice);
        return "tgls/system/Invoice/invoice_vive";
    }
    @RequestMapping("look")
    public String lookInvoice(String id,Map<String, Object> map) {
        Invoice invoice = invoiceMapper.selectByPrimaryKey(id);
        map.put("invoice",invoice);
        return "tgls/system/Invoice/invoice_look";
    }

    @RequestMapping("SFkt")
    public String lookInvoice(String id,String order,Map<String, Object> map) {
        Invoice invoice = new Invoice();
        invoice.setId(id);
        invoice.setState("3");
        invoice.setExpress(order);
        invoiceMapper.updateByPrimaryKeySelective(invoice);
        return "redirect:/invoice";
    }

    @RequestMapping("update")
    public String uodateInvoice(String id){
        Invoice invoice = new Invoice();
        invoice.setId(id);
        invoice.setState("2");
        invoiceMapper.updateByPrimaryKeySelective(invoice);
        return "redirect:/invoice";
    }
}
