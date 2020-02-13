package com.haozhi.greenroom.controller;

import com.haozhi.greenroom.dao.BrandNormMapper;
import com.haozhi.greenroom.dao.InvitationMapper;
import com.haozhi.greenroom.pojo.BrandNorm;
import com.haozhi.greenroom.pojo.Invitation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("brandnorm")
public class BrandNormController {


    @Autowired
    private BrandNormMapper brandNormMapper;

    @RequestMapping
    public String Invitation(Map<Object,Object> map){
        List<BrandNorm> brandNorm = brandNormMapper.selectAll();
        map.put("list",brandNorm.get(0));
        return "tgls/system/brandNorm/brandNorm_list";
    }


    @RequestMapping(value = "update",method = RequestMethod.POST)
    public String RegisterUpload(BrandNorm brandNorm){
        brandNormMapper.updateByPrimaryKeySelective(brandNorm);
        return "redirect:/brandnorm";
    }
}
