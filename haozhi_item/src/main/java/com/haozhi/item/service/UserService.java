package com.haozhi.item.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.haozhi.common.enums.ExceptionEnum;
import com.haozhi.common.exception.LyException;
import com.haozhi.common.utils.IdWorker;
import com.haozhi.item.config.OpenIdProperties;
import com.haozhi.item.dao.*;
import com.haozhi.item.pojo.*;
import com.haozhi.item.utils.HttpClientUtil;
import com.haozhi.item.utils.HttpService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author kgy
 * @version 1.0
 * @date 2019/12/27 14:08
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegardMapper regardMapper;

    @Autowired
    private BankMapper bankMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private InvoiceMapper invoiceMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private OpenIdProperties openIdProperties;

    /**
     * 查询 openid是否存在
     *
     * @param userPwd
     * @return
     */
    public User selectByOpenid(String userPwd) {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("openid", userPwd);
        return userRepository.selectOneByExample(example);
    }

    /**
     * 根据session 获取id查询用户
     *
     * @param userId
     * @return
     */
    public User queryById(Object userId) {
        return userRepository.selectByPrimaryKey(userId);
    }

    public User personalSettings(String id) {
        return userRepository.selectByPrimaryKey(id);
    }

    /**
     * 修改用户信息
     *
     * @param user
     */
    public void updateSettings(User user) {
        userRepository.updateByPrimaryKeySelective(user);
    }

    public Regard personalRegard() {
        return regardMapper.selectByPrimaryKey(1);
    }

    /**
     * 查询优惠券
     *
     * @param id
     * @return
     */
    public User queryVip(String id) {
        return userRepository.selectByPrimaryKey(id);
    }

    /**
     * 查询银行卡
     *
     * @param id
     * @return
     */
    public Bank queryBank(String id) {
        Example example = new Example(Bank.class);
        example.createCriteria().andEqualTo("userId", id);
        return bankMapper.selectOneByExample(example);
    }

    /**
     * 添加银行卡
     *
     * @param bank
     */
    public void personalAdd(Bank bank) {
        bank.setId(idWorker.nextId() + "");
        bankMapper.insert(bank);
    }

    public List<Order> personalOrder(String id) {
        Example example = new Example(Order.class);
        example.createCriteria().andEqualTo("userId", id).andEqualTo("invoiceState", 1).andEqualTo("state", 3);
        return orderMapper.selectByExample(example);
    }

    /**
     * @param type
     * @param page
     * @param rows
     * @return
     */
    public List<Invoice> personalInvoice(String type, Integer page, Integer rows, User user) {
        if ("1".equals(type)) {
            Example example = new Example(Invoice.class);
            example.createCriteria().andEqualTo("state", type).andEqualTo("userId", user.getId());
            example.createCriteria().andEqualTo("userId", user.getId());
            PageHelper.startPage(page, rows);
            return invoiceMapper.selectByExample(example);
        } else if ("2".equals(type)) {
            Example example = new Example(Invoice.class);
            example.createCriteria().andEqualTo("state", type).andEqualTo("userId", user.getId());
            example.createCriteria().andEqualTo("userId", user.getId());
            PageHelper.startPage(page, rows);
            return invoiceMapper.selectByExample(example);
        } else if ("3".equals(type)) {
            Example example = new Example(Invoice.class);
            example.createCriteria().andEqualTo("state", type).andEqualTo("userId", user.getId());
            example.createCriteria().andEqualTo("userId", user.getId());
            PageHelper.startPage(page, rows);
            return invoiceMapper.selectByExample(example);
        }
        return null;
    }

    public void personalSaveOrder(Invoice invoice, User user) {
        invoice.setId(idWorker.nextId() + "");
        invoice.setTime(new Date());
        invoice.setState("1");
        invoice.setUserId(user.getId());
        invoice.setUserName(user.getName());
        String[] split = invoice.getInvoiceOrder().split(",");
        if (!split.equals("") && split != null) {
            for (String s : split) {
                Order order = new Order();
                order.setId(s);
                order.setInvoiceState("2");
                order.setInvoiceTime(new Date());
                orderMapper.updateByPrimaryKeySelective(order);
            }
        }
        invoiceMapper.insert(invoice);
    }

    public void personalUpdate(Bank bank) {
        bankMapper.updateByPrimaryKeySelective(bank);
    }

    public Bank personalEcho(String id) {
        Example example = new Example(Bank.class);
        example.createCriteria().andEqualTo("userId", id);
        return bankMapper.selectOneByExample(example);
    }

    /**
     * 授权获取 openid
     *
     * @param code
     * @return
     */
    public User getOpenidByCode(String code) {

        Map map = new HashMap<>();
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + openIdProperties.getAPPID() + "&secret=" + openIdProperties.getSECRET() + "&code=" + code + "&grant_type=authorization_code";
/*        String str = HttpService.httpRequest(url, "GET", null);
        JSONObject jsonObject = JSONObject.parseObject(str);*/

        String result = HttpClientUtil.doGet(url);

        System.out.println("请求获取access_token:" + result);
        //返回结果的json对象
        JSONObject jsonObject = JSON.parseObject(result);

        String openid = "";
        if (result.indexOf("errcode") != -1) {
            throw new LyException(ExceptionEnum.WX_ERROR);
        } else {
            openid = jsonObject.get("openid").toString();
        }

        /**
         * 判断是否存在已有openid
         *      不存在 跳登录页面进行登录注册
         *      存在 根据openid 查询用户信息放在 session中
         */
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("openid", openid);
        User loginUser = userRepository.selectOneByExample(example);
        /**
         * 不存在
         */
        if (loginUser == null) {
            //请求获取userInfo
            String infoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=" + jsonObject.getString("access_token") +
                    "&openid=" + jsonObject.getString("openid") +
                    "&lang=zh_CN";

            String resultInfo = HttpClientUtil.doGet(infoUrl);
            JSONObject user = JSON.parseObject(resultInfo);
            //需要传入 微信名字跟微信的头像
            loginUser = new User();
            loginUser.setOpenid(openid);
            loginUser.setWxImage(user.get("headimgurl").toString());
            loginUser.setWxName(user.get("nickname").toString());
            return loginUser;
        } else {
            return loginUser;
        }

    }

    @Autowired
    private MonthOrderMapper monthOrderMapper;
    @Autowired
    private AccountMapper accountMapper;

    /**
     * 月订单 查询
     *
     * @param user
     * @return
     */
    public List<Account> monthOrder(User user) {
        // java 代码如何获取当前时间的上一个月的月末时间..
        Calendar cal = Calendar.getInstance();
        // 设置天数为-1天，表示当月减一天即为上一个月的月末时间
        cal.set(Calendar.DAY_OF_MONTH, -1);
        //格式化输出年月日
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        /**
         * 上个月最后一天的日期
         */
        Date lastTime = cal.getTime();
        Date userTime = user.getTime();
        String format = sdf.format(lastTime);
        /**
         * compareTo()方法的返回值，date1小于date2返回-1，date1大于date2返回1，相等返回0
         */
        int i = userTime.compareTo(lastTime);
        /**
         * 注册时间大于上个月时间不进行查询
         */
        AtomicReference<Integer> monthPrice = new AtomicReference<>(0);
        if (i != -1) {
            Example accountEx = new Example(Account.class);
            accountEx.createCriteria().andEqualTo("userId", user.getId()).andLike("time", "%" + format + "%");
            List<Account> accounts = accountMapper.selectByExample(accountEx);
            /**
             * accounts 等于 null说明上个月的还没查询
             */
            if (accounts.size() == 0) {
                Example example = new Example(MonthOrder.class);
                example.createCriteria().andEqualTo("userId", user.getId()).andLike("time", "%" + format + "%");
                List<MonthOrder> monthOrders = monthOrderMapper.selectByExample(example);
                monthOrders.forEach(monthOrder -> {
                    monthPrice.updateAndGet(v -> v + monthOrder.getPrice());
                });
                Account account = new Account();
                account.setId(idWorker.nextId() + "");
                account.setPrice(monthPrice.get());
                account.setState("1");
                account.setUserId(user.getId());
                account.setTime(lastTime);
                /**
                 * 进行保存上个月的 金额
                 */
                accountMapper.insert(account);
            }
        }
        Example queryEx = new Example(Account.class);
        queryEx.setOrderByClause(" state desc ");
        queryEx.setOrderByClause(" time desc ");
        queryEx.createCriteria().andEqualTo("userId", user.getId());
        return accountMapper.selectByExample(queryEx);
    }

    /**
     * 修改 提现状态
     *
     * @param id
     */
    public void requestWithdrawal(String id) {
        Account account = new Account();
        account.setId(id);
        account.setState("2");
        accountMapper.updateByPrimaryKeySelective(account);
    }

    /**
     * 注册根据 用户填写的 手机号 进行校验
     *
     * @param tel
     * @return
     */
    public List<User> querytel(String tel) {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("tel", tel);
        return userRepository.selectByExample(example);
    }

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 短信验证码  验证
     *
     * @param code
     * @param tel
     * @return
     */
    public boolean querySms(String code, String tel) {
        //判断用户的验证码和redis中一致，才可以注册
        String codeRedis = redisTemplate.boundValueOps(tel).get();
        //通过StringUtils比较两个字符串是否一致
        if (!StringUtils.equals(codeRedis, code)) {
            return false;
        }
        return true;
    }

    /**
     * 保存 注册用户信息
     *
     * @param user
     */
    public void saveUser(User user) {
        user.setId(idWorker.nextId() + "");
        user.setTime(new Date());
        user.setState("1");
        user.setCoupon(0);
        user.setBalance(0);
        if (user.getSuperId() != null && !user.getSuperId().equals("")) {
            User user1 = userRepository.selectByPrimaryKey(user.getSuperId());
            user1.setCoupon(user1.getCoupon() + 1);
            userRepository.updateByPrimaryKeySelective(user1);
        }
        userRepository.insert(user);
    }

    public void updateState(String id) {
        User user = new User();
        user.setState("1");
        user.setId(id);
        userRepository.updateByPrimaryKeySelective(user);
    }
}
