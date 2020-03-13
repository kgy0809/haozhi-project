package com.haozhi.item.web.controller;

import com.haozhi.common.constants.StatusCode;
import com.haozhi.common.dto.ResultDTO;
import com.haozhi.item.dao.BusinessTwoMapper;
import com.haozhi.item.dto.LastDto;
import com.haozhi.item.dto.MenuDto;
import com.haozhi.item.pojo.BusinessTwo;
import com.haozhi.item.pojo.HzYw;
import com.haozhi.item.pojo.Menu;
import com.haozhi.item.pojo.User;
import com.haozhi.item.service.HzYwService;
import com.haozhi.item.service.MenuService;
import com.haozhi.item.service.UploadService;
import com.haozhi.item.service.UserService;
import com.haozhi.item.web.common.BaseController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.persistence.Id;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author kgy
 * @version 1.0
 * @date 2019/12/26 18:11
 */
@Controller
@RequestMapping("business")
public class BusinessController extends BaseController {

    @Autowired
    private HzYwService hzYwService;

    @Autowired
    private UserService userService;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private BusinessTwoMapper businessTwoMapper;

    /**
     * 页面的跳转
     *
     * @return
     */
    @RequestMapping
    public String business(String id, Map<String, Object> map) {
        map.put("user", userService.queryById(getUser().getId()));
        List<HzYw> list = hzYwService.queryById(id);
        map.put("list", list);
        return "civil/business";
    }

    /**
     * two 跳转
     *
     * @param id
     * @return
     */
    @RequestMapping("/two")
    public String businessTwo(String id) {
        /**
         * oneId    标签 name id
         */
        session.setAttribute("oenId", id);
        return "civil/business_order_ok";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO businessUpload(MultipartFile file) {
        String url = uploadService.insertImage(file);
        return new ResultDTO(true, StatusCode.OK, "上传成功", url);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String businessTextTwo(BusinessTwo businessTwo, Map<String, Object> map) {
        String oneId = (String) session.getAttribute("oenId");
        HzYw hzYw = hzYwService.queryGYById(oneId);
        businessTwo.setOneId(oneId);
        String twoId = hzYwService.businessTextTwo(businessTwo);
        /**
         * twoId  订单ID
         */
        session.setAttribute("twoId", twoId);
        /**
         * 国内页面跳转
         */
        List<Menu> menus = menuService.queryOne();
        if (hzYw.getZid().equals("10086")) {
            map.put("menu", menus);
            return "civil/business_category";
        } else {
            /*map.put("menu", menus);*/
            map.put("user", userService.queryById(getUser().getId()));
            return "civil/business_applicant";
        }
    }

    /**
     * 查询一级菜单 下面的其他（国内）
     *
     * @param id
     * @return
     */
    @PostMapping("MenusTwo/{id}")
    @ResponseBody
    public ResultDTO businessMenusTwo(@PathVariable("id") String id) {
        User user = getUser();
        String twoId = (String) session.getAttribute("twoId");
        List<MenuDto> menuDtos = menuService.queryTwo(id, twoId, user.getState());
        return new ResultDTO(true, StatusCode.OK, "查询成功", menuDtos);
    }

    /**
     * 国际一级菜单跳转
     *
     * @return
     */
    @RequestMapping(value = "GgTwo/{id}")
    public String businessGgTwo(@PathVariable String id, Map<String, Object> map) {
        map.put("user", userService.queryById(getUser().getId()));
        String twoId = (String) session.getAttribute("twoId");
        hzYwService.updateGgTwo(id, twoId);
        return "civil/business_applicant";
    }

    /**
     * 页面跳转
     *
     * @return
     */
    @RequestMapping("three")
    public String three(Map<String, Object> map) {
        String twoId = (String) session.getAttribute("twoId");
        BusinessTwo businessTwo = businessTwoMapper.selectByPrimaryKey(twoId);
        map.put("businessTwo",businessTwo.getOneId());
        map.put("id", twoId);
        map.put("user", userService.queryById(getUser().getId()));
        return "civil/business_applicant";
    }

    /**
     * 跳转 添加
     *
     * @param menuId
     * @param price
     * @return
     */
    @RequestMapping(value = "/save/three", method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO businessSaveThree(String menuId, Integer price) {
        String twoId = (String) session.getAttribute("twoId");
        hzYwService.updateThree(twoId, menuId, price);
        return new ResultDTO(true, StatusCode.OK, "添加成功");
    }

    @RequestMapping(value = "/save/four", method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO businessSaveFour(BusinessTwo dataArr) {
        String twoId = (String) session.getAttribute("twoId");
        hzYwService.updateFour(dataArr, twoId);
        return new ResultDTO(true, StatusCode.OK, "保存成功");
    }

    /**
     * 下载 word 委托书
     *
     * @return
     */
    @RequestMapping("downloadOf")
    @ResponseBody
    public ResultDTO downloadWord() {
        String twoId = (String) session.getAttribute("twoId");
        String wordUrl = hzYwService.downloadWord(twoId);
        if (wordUrl == null) {
            return new ResultDTO(true, StatusCode.OK, "查询成功,没有委托书", null);
        }
        return new ResultDTO(true, StatusCode.OK, "查询成功", wordUrl);
    }
    /**
     * 发邮件 word 委托书
     *
     * @return
     */
    @RequestMapping("emile")
    @ResponseBody
    public ResultDTO emile(String emile) {
        String twoId = (String) session.getAttribute("twoId");
        String emile1 = hzYwService.emile(twoId, emile);
/*        if (wordUrl == null) {
            return new ResultDTO(true, StatusCode.OK, "查询成功,没有委托书", null);
        }*/
        if (emile1 == null) {
            return new ResultDTO(true, StatusCode.OK, "查询成功,没有委托书", null);
        }
        return new ResultDTO(true, StatusCode.OK, "发送成功","发送成功");
    }

    @RequestMapping("last")
    public String businessLast(Map<String, Object> map) {
        String twoId = (String) session.getAttribute("twoId");
        LastDto lastDto = hzYwService.businessLast(twoId, getUser());
        String oneId = (String) session.getAttribute("oenId");
        HzYw hzYw = hzYwService.queryGYById(oneId);
        map.put("list", lastDto);
        map.put("hzYw", hzYw);
        return "civil/business_information";
    }

    /**
     * 生成 合同 （急速注册，保险注册，担保注册）
     *
     * @return
     */
    @RequestMapping(value = "downloadContractCN", method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO downloadContract(String type, String email,String id) throws MessagingException, IOException {
        String twoId = (String) session.getAttribute("twoId");
        String contract = hzYwService.downloadContract(twoId, getUser(), type, email, id);
        if (contract == null){
            return new ResultDTO(true, StatusCode.REPERROR, "没有合同供下载");
        }
        return new ResultDTO(true, StatusCode.OK, "合同下载成功", contract);
    }



    @RequestMapping("etc")
    public String etc(Map<String, Object> map) {
        String twoId = (String) session.getAttribute("twoId");
        BusinessTwo etc = hzYwService.etc(twoId);
        etc.setId(null);
        map.put("etc", etc);
        return "order/my_order_contract";
    }

    @RequestMapping("updateEtc")
    public String updateEtc(BusinessTwo businessTwo){
        String twoId = (String) session.getAttribute("twoId");
        hzYwService.updateEtc(twoId,businessTwo);
        return "redirect:/business/etc";
    }

}

