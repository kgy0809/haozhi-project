package com.haozhi.item.web.controller;

import com.haozhi.item.dao.BrandNormMapper;
import com.haozhi.item.pojo.BrandNorm;
import com.haozhi.item.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("brandNorm")
public class BrandNormController extends BaseController {

    @Autowired
    private BrandNormMapper brandNormMapper;

    @RequestMapping
    public String BrandNorm(Map<Object, Object> map) {
        List<BrandNorm> brandNorms = brandNormMapper.selectAll();
        map.put("list",brandNorms.get(0));
        return "civil/brand_norm";
    }
}
