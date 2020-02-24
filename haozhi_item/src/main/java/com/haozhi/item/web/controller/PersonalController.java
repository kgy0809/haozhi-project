package com.haozhi.item.web.controller;

import com.google.zxing.WriterException;
import com.haozhi.common.constants.StatusCode;
import com.haozhi.common.dto.ResultDTO;
import com.haozhi.item.dao.AccountMapper;
import com.haozhi.item.dao.BankMapper;
import com.haozhi.item.dao.VipTimeMapper;
import com.haozhi.item.dto.NumDto;
import com.haozhi.item.pojo.*;
import com.haozhi.item.service.OrderService;
import com.haozhi.item.service.UserService;
import com.haozhi.item.service.VipService;
import com.haozhi.item.utils.QRCodeGenerator;
import com.haozhi.item.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author kgy
 * @version 1.0 my 我的页面
 * @date 2019/12/26 18:29
 */
@Controller
@RequestMapping("personal")
public class PersonalController extends BaseController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;
    @Autowired
    private VipTimeMapper vipTimeMapper;

    @Autowired
    private BankMapper bankMapper;

    @Autowired
    private AccountMapper accountMapper;

    /**
     * 我的页面查询  会 计算会员的到期时间
     *
     * @param map
     * @return
     */
    @RequestMapping
    public String personal(Map<String, Object> map) {
        User user = userService.queryById(getUser().getId());
        if (user.getState().equals("2")) {
            VipTime vipTime = vipTimeMapper.selectByPrimaryKey(user.getVipTimeId());
            /**
             * 到期时间
             */
            Date expireTime = vipTime.getExpireTime();
            long quot = 0;
            try {
                quot = expireTime.getTime() - new Date().getTime();
                quot = quot / 1000 / 60 / 60 / 24;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (quot > 0) {
                /**
                 * vip 到期时间
                 */
                map.put("svipTime", vipTime.getSexpireTime());
            } else {
                userService.updateState(user.getId());
            }
        }
        NumDto numDto = orderService.queryCountNumNew(user.getId());
        map.put("numDto", numDto);
        map.put("user", userService.queryById(user.getId()));
        return "personal/personal";
    }

    /**
     * 回显用户信息
     *
     * @param map
     * @return
     */
    @RequestMapping("settings")
    public String personalSettings(Map<String, Object> map) {
        map.put("user", userService.queryById(getUser().getId()));
        return "personal/personal_Settings";
    }

    /**
     * 修改用户信息
     *
     * @param user
     */
    @RequestMapping(value = "/update/settings", method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO personalUpdateSettings(User user) {
        user.setId(getUser().getId());
        User oldUser = userService.queryById(getUser().getId());
        if (!oldUser.getTel().equals(user.getTel())){
            List<User> tel = userService.querytel(user.getTel());
            if (tel != null && tel.size() > 0)
                return new ResultDTO(true, StatusCode.LOGINERROR, "手机号已经存在");
            if (!userService.querySms(user.getCode(), user.getTel()))
                return new ResultDTO(true, StatusCode.ACCESSERROR, "验证码错误");
        }
        userService.updateSettings(user);
        return new ResultDTO(true, StatusCode.OK, "修改成功");
    }

    /**
     * 关于
     *
     * @param map
     * @return
     */
    @RequestMapping("regard")
    public String personalRegard(Map<String, Object> map) {
        Regard regard = userService.personalRegard();
        map.put("regard", regard);
        return "personal/personal_about";
    }

    @Autowired
    private VipService vipService;

    /**
     * 优惠券 数量
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "vip")
    public String personalVip(Map<String, Object> map) {
        User user = userService.queryVip(getUser().getId());
        map.put("user", user);
        List<Vip> vips = vipService.queryAll();
        Vip vip = vips.get(0);
        map.put("vip", vip);
        return "personal/personal_vip";
    }

    /**
     * 收益
     *
     * @param map
     * @return
     */
    @RequestMapping("card")
    public String personalCard(Map<String, Object> map) {
        Bank bank = userService.queryBank(getUser().getId());
        map.put("bank", bank);
        return "personal/personal_card";
    }

    /**
     * 添加银行卡
     *
     * @param bank
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public ResultDTO personalAdd(Bank bank) {
        bank.setUserId(getUser().getId());
        userService.personalAdd(bank);
        return new ResultDTO(true,StatusCode.OK,"添加成功");
    }

    @RequestMapping("each")
    public String personalEcho(Map<String, Object> map) {
        Bank bank = userService.personalEcho(getUser().getId());
        map.put("bank", bank);
        return "personal/personal_each";
    }

    /**
     * 修改银行卡
     *
     * @param bank
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultDTO personalUpdate(Bank bank) {
        userService.personalUpdate(bank);
        return new ResultDTO(true,StatusCode.OK,"修改成功");
    }

    @RequestMapping("personalInvoice")
    public String personalInvoiceT() {
        return "personal/personal_invoice";
    }

    /**
     * 查询开票
     *
     * @param type
     * @return
     */
    @RequestMapping(value = "invoice", method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO personalInvoice(
            @RequestParam(name = "type") String type,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "10") Integer rows
    ) {
        List<Invoice> invoices = userService.personalInvoice(type, page, rows, getUser());
        /*if (invoices == null) {
            return new ResultDTO(true, StatusCode.OK, "type传入错误");
        }*/
        return new ResultDTO(true, StatusCode.OK, "查询成功", invoices);
    }

    @RequestMapping("apply")
    public String apply(Map<String, Object> map) {
        List<Order> order = userService.personalOrder(getUser().getId());
        map.put("order", order);
        return "personal/personal_apply_For";
    }

    /**
     * 开票
     *
     * @param invoice
     * @return
     */
    @RequestMapping(value = "save/apply", method = RequestMethod.POST)
    public String personalSaveOrder(Invoice invoice) {
        userService.personalSaveOrder(invoice, getUser());
        return "redirect:/personal/personalInvoice";
    }

    @RequestMapping("code")
    public String personalCode() {
        return "personal/my_code";
    }

    /**
     * 生成二维码
     *
     * @return
     */
    @GetMapping(value = "/qrimage")
    public ResponseEntity<byte[]> getQRImage() {
        String URL = "http://haozhiqiye.haozhizixun.com/api/login?superId=" + getUser().getId();
        //二维码内的信息
        String info = URL;

        byte[] qrcode = null;
        try {
            qrcode = QRCodeGenerator.getQRCodeImage(info, 300, 300);
        } catch (WriterException e) {
            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
        }

        // Set headers
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<byte[]>(qrcode, headers, HttpStatus.CREATED);
    }

    /**
     * 查询提现订单
     *
     * @return
     */
    @RequestMapping(value = "monthOrder")
    @ResponseBody
    public ResultDTO monthOrder() {
        List<Account> accounts = userService.monthOrder(getUser());
        return new ResultDTO(true, StatusCode.OK, "查询成功", accounts);
    }

    /**
     * 提现点击
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "requestWithdrawal", method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO requestWithdrawal(String id) {
        Account account = accountMapper.selectByPrimaryKey(id);
        Example example = new Example(Bank.class);
        example.createCriteria().andEqualTo("userId", account.getUserId());
        List<Bank> banks = bankMapper.selectByExample(example);
        if (banks.size() == 0) {
            return new ResultDTO(true, StatusCode.LOGINERROR, "请先绑定银行卡");
        }
        userService.requestWithdrawal(id);
        return new ResultDTO(true, StatusCode.OK, "提现修改成功");
    }

}
