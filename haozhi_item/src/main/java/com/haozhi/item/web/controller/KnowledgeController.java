package com.haozhi.item.web.controller;

import com.haozhi.item.dao.KnowledgeMapper;
import com.haozhi.item.pojo.Knowledge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("knowledge")
public class KnowledgeController {

    @Autowired
    private KnowledgeMapper knowledgeMapper;

    @RequestMapping
    public String Knowledge(Map<String, Object> map) {
        List<Knowledge> knowledge = knowledgeMapper.selectAll();
        map.put("knowledge", knowledge.get(0));
        return "civil/knowledge_norm";
    }
}
