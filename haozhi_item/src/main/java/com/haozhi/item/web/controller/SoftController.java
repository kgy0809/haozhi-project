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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.security.x509.RDN;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author kgy
 * @version 1.0
 * @date 2019/12/26 18:17
 */
@Controller
@RequestMapping("soft")
public class SoftController extends BaseController {

    @Autowired
    private HzYwService hzYwService;
    @Autowired
    private UserService userService;
    /**
     * 页面跳转
     * @return
     */
    @RequestMapping
    public String soft(String id,Map<String ,Object> map){
        map.put("user",userService.queryById(getUser().getId()));
        map.put("id",id);
/*        List list = hzYwService.queryById(id);
        map.put("list",list);*/
        return "soft/soft";
    }

    /**
     * ajax 接口
     * @param id    zid
     * @param sId   1 或2
     * @return
     */
    @RequestMapping(value = "/listId/{id}/{sId}",method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO softList(@PathVariable("id") String id, @PathVariable("sId") String sId){
        List list = hzYwService.softListById(id,sId);
        return new ResultDTO(true, StatusCode.OK,"查询成功",list);
    }

    /**
     * 根据id查询信息
     * @param id    自己的id
     * @return
     */
    @RequestMapping(value = "/numb/{Id}",method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO softListId(@PathVariable("Id") String id){
        HzYw hzYw = hzYwService.softById(id);
        return new ResultDTO(true, StatusCode.OK,"查询成功",hzYw);
    }

    /**
     * two 跳转
     * @param id
     * @return
     */
    @RequestMapping("/two")
    public String businessTwo(String id,Map<String,Object> map){
        HzYw hzYw = hzYwService.softNumbById(id,getUser());
        session.setAttribute("softId",hzYw.getId());
        map.put("hzYw",hzYw);
        map.put("user",userService.queryById(getUser().getId()));
        return "soft/solt_applicant";
    }

    /**
     * 下载 word 委托书
     * @return
     */
    @RequestMapping("downloadOf")
    @ResponseBody
    public ResultDTO downloadWord(){
        String softId = (String) session.getAttribute("softId");
        String wordUrl = hzYwService.downloadWord(softId);
        if (wordUrl == null){
            return new ResultDTO(true,StatusCode.OK,"查询成功,没有委托书",null);
        }
        return new ResultDTO(true,StatusCode.OK,"查询成功",wordUrl);
    }

    /**
     * 发邮件 word 委托书
     *
     * @return
     */
    @RequestMapping("emile")
    @ResponseBody
    public ResultDTO emile(String emile) {
        String softId = (String) session.getAttribute("softId");
        hzYwService.emile(softId,emile);
/*        if (wordUrl == null) {
            return new ResultDTO(true, StatusCode.OK, "查询成功,没有委托书", null);
        }*/
        return new ResultDTO(true, StatusCode.OK, "发送成功");
    }

    @RequestMapping(value = "save",method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO softSave(BusinessTwo businessTwo){
        String softId = (String)session.getAttribute("softId");
        hzYwService.updateFour(businessTwo,softId);
        return new ResultDTO(true,StatusCode.OK,"保存成功");
    }

    @RequestMapping("last")
    public String softSave(Map<String,Object> map){
        String softId = (String)session.getAttribute("softId");
        LastDto lastDto = hzYwService.updateLast(softId);
        map.put("list",lastDto);
        return "soft/solt_information";
    }

    @RequestMapping("etc")
    public String etc(Map<String, Object> map) {
        String twoId = (String) session.getAttribute("softId");
        BusinessTwo etc = hzYwService.etc(twoId);
        etc.setId(null);
        map.put("etc", etc);
        return "soft/my_soft_contract";
    }

    @RequestMapping("updateEtc")
    public String updateEtc(BusinessTwo businessTwo){
        String twoId = (String) session.getAttribute("softId");
        hzYwService.updateEtc(twoId,businessTwo);
        return "redirect:/soft/etc";
    }

    /**
     * 生成 合同 （）
     *
     * @return
     */
    @RequestMapping(value = "downloadContractCN", method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO downloadContract(String type, String email,String id) throws MessagingException, IOException {
        String softId = (String) session.getAttribute("softId");
        String contract = hzYwService.softDownloadContract(softId, getUser(), type, email,id);
        if (contract == null)
            return new ResultDTO(true, StatusCode.OK, "没有合同供下载", null);
        return new ResultDTO(true, StatusCode.OK, "合同下载成功", contract);
    }

}
