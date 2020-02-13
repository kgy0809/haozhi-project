package com.haozhi.greenroom.controller;

import com.haozhi.greenroom.dao.InvitationMapper;
import com.haozhi.greenroom.pojo.Invitation;
import com.haozhi.greenroom.pojo.RegisterAgreement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("invitation")
public class InvitationController {

    @Autowired
    private InvitationMapper invitationMapper;

    @RequestMapping
    public String Invitation(Map<Object,Object>map){
        List<Invitation> invitations = invitationMapper.selectAll();
        map.put("list",invitations.get(0));
        return "tgls/system/invitation/invitation_list";
    }


    @RequestMapping(value = "update",method = RequestMethod.POST)
    public String RegisterUpload(Invitation invitation){
        invitationMapper.updateByPrimaryKeySelective(invitation);
        return "redirect:/invitation";
    }
}
