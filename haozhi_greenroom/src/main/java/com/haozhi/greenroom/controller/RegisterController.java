package com.haozhi.greenroom.controller;

import com.haozhi.greenroom.dao.RegisterAgreementMapper;
import com.haozhi.greenroom.pojo.RegisterAgreement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("register")
public class RegisterController {

    @Autowired
    private RegisterAgreementMapper registerAgreementMapper;

    @RequestMapping
    public String Register(Map<Object,Object>map) {
        List<RegisterAgreement> registerAgreements = registerAgreementMapper.selectAll();
        map.put("list",registerAgreements.get(0));
        return "tgls/system/register/register_list";
    }

    @RequestMapping(value = "update",method = RequestMethod.POST)
    public String RegisterUpload(RegisterAgreement registerAgreement){
        registerAgreementMapper.updateByPrimaryKeySelective(registerAgreement);
        return "redirect:/register";
    }
}
