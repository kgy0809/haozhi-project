package com.haozhi.item.web.controller;

import com.haozhi.common.constants.StatusCode;
import com.haozhi.common.dto.ResultDTO;
import com.haozhi.item.dto.LastDto;
import com.haozhi.item.pojo.BusinessTwo;
import com.haozhi.item.pojo.HzYw;
import com.haozhi.item.pojo.User;
import com.haozhi.item.service.HzYwService;
import com.haozhi.item.service.UserService;
import com.haozhi.item.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/2 10:22
 */
@Controller
@RequestMapping("copyright")
public class CopyrightController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private HzYwService hzYwService;

    /**
     * 首页跳转
     * @return
     */
    @RequestMapping
    public String indexList(String id, Map<String,Object> map){
        map.put("user", getUser());
        List<HzYw> list = hzYwService.queryById(id);
        map.put("list", list);
        return "copyright/right";
    }

    /**
     * two 跳转
     * @param id
     * @return
     */
    @RequestMapping("/two")
    public String businessTwo(String id,Map<String,Object> map){
        HzYw hzYw = hzYwService.softNumbById(id,getUser());
        session.setAttribute("copyrightId",hzYw.getId());
        map.put("hzYw",hzYw);
        map.put("user",getUser());
        return "copyright/right_applicant";
    }

    /**
     * 下载 word 委托书
     * @return
     */
    @RequestMapping("downloadOf")
    @ResponseBody
    public ResultDTO downloadWord(){
        String copyrightId = (String) session.getAttribute("copyrightId");
        String wordUrl = hzYwService.downloadWord(copyrightId);
        if (wordUrl == null){
            return new ResultDTO(true, StatusCode.OK,"查询成功,没有委托书",null);
        }
        return new ResultDTO(true,StatusCode.OK,"查询成功",wordUrl);
    }
    @RequestMapping(value = "save",method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO softSave(BusinessTwo businessTwo){
        String copyrightId = (String)session.getAttribute("copyrightId");
        hzYwService.updateFour(businessTwo,copyrightId);
        return new ResultDTO(true,StatusCode.OK,"保存成功");
    }

    @RequestMapping(value = "last")
    public String softSave(Map<String,Object> map){
        String copyrightId = (String)session.getAttribute("copyrightId");
        LastDto lastDto = hzYwService.updateLast(copyrightId);
        map.put("list",lastDto);
        return "copyright/right_information";
    }

    @RequestMapping("etc")
    public String etc(Map<String, Object> map) {
        String twoId = (String) session.getAttribute("copyrightId");
        BusinessTwo etc = hzYwService.etc(twoId);
        map.put("etc", etc);
        etc.setId(null);
        return "copyright/my_copyright_contract";
    }

    @RequestMapping("updateEtc")
    public String updateEtc(BusinessTwo businessTwo){
        String twoId = (String) session.getAttribute("copyrightId");
        hzYwService.updateEtc(twoId,businessTwo);
        return "redirect:/copyright/etc";
    }

    /**
     * 生成 合同 （）
     *
     * @return
     */
    @RequestMapping(value = "downloadContractCN", method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO downloadContract(String type, String email,String id) throws MessagingException, IOException {
        String copyrightId = (String) session.getAttribute("copyrightId");
        String contract = hzYwService.copyrightDownloadContract(copyrightId, getUser(), type, email,id);
        if (contract == null)
            return new ResultDTO(true, StatusCode.OK, "没有合同供下载", null);
        return new ResultDTO(true, StatusCode.OK, "合同下载成功", contract);
    }

}
