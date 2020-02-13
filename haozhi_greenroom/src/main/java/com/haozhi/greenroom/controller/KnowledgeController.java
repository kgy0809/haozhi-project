package com.haozhi.greenroom.controller;

import com.haozhi.greenroom.dao.InvitationMapper;
import com.haozhi.greenroom.dao.KnowledgeMapper;
import com.haozhi.greenroom.pojo.Invitation;
import com.haozhi.greenroom.pojo.Knowledge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("knowledge")
public class KnowledgeController {

    @Autowired
    private KnowledgeMapper knowledgeMapper;

    @RequestMapping
    public String Invitation(Map<Object,Object> map){
        List<Knowledge> knowledges = knowledgeMapper.selectAll();
        map.put("list",knowledges.get(0));
        return "tgls/system/knowledge/knowledge_list";
    }


    @RequestMapping(value = "update",method = RequestMethod.POST)
    public String RegisterUpload(Knowledge knowledge){
        knowledgeMapper.updateByPrimaryKeySelective(knowledge);
        return "redirect:/knowledge";
    }
}
