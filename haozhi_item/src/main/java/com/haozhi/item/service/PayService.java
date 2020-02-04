package com.haozhi.item.service;

import com.haozhi.common.enums.ExceptionEnum;
import com.haozhi.common.exception.LyException;
import com.haozhi.common.utils.IdWorker;
import com.haozhi.item.dao.*;
import com.haozhi.item.pojo.*;
import com.haozhi.item.utils.sdk.WXPayOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/11 8:34
 */
@Service
public class PayService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private BusinessTwoMapper businessTwoMapper;
    @Autowired
    private HzYwRepository hzYwRepository;
    @Autowired
    private HzZcRepository hzZcRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VipTimeMapper vipTimeMapper;
    @Autowired
    private MonthOrderMapper monthOrderMapper;

    @Autowired
    private IdWorker idWorker;

    public Map<String, Object> createOrder(User user, String bid, Integer pay, String remark) {
        BusinessTwo business = businessTwoMapper.selectByPrimaryKey(bid);
        HzYw hzYw = hzYwRepository.selectByPrimaryKey(business.getOneId());
        Order order = new Order();
        order.setId(idWorker.nextId() + "");
        order.setTime(new Date());
        order.setRemark(remark);
        order.setUserId(user.getId());
        if ("1".equals(user.getState()))
            order.setFwPrice(hzYw.getHyPrice() + business.getCommission());
        if ("2".equals(user.getState()))
            order.setFwPrice(hzYw.getVipPrice() + business.getCommission());
        order.setGfPrice(hzYw.getGfPrice());
        order.setPrice(pay);
        order.setNumber(business.getNumber());
        HzZc hzZc = hzZcRepository.selectByPrimaryKey(hzYw.getZid());
        order.setTitle(hzZc.getName());
        order.setSbName(hzYw.getName());
        order.setPOrder(bid);
        order.setOpenid(user.getOpenid());
        order.setState(1);
        order.setInvoiceState("1");
        order.setNumber(business.getNumber());
        order.setXxPayStart("2");
        orderMapper.insert(order);
        /**
         * 微信支付
         */

        WXPayOrder wxPayOrder = new WXPayOrder(order);
        Map<String, Object> str = null;
        try {
            str = wxPayOrder.getParameter();
        } catch (Exception e) {
            throw new LyException(ExceptionEnum.INVALID_ORDER_STATUS);
        }
        return str;
    }

    /**
     * 回调返回的 订单id
     */
    public void queryOrder(String out_trade_no) {
        Order order = orderMapper.selectByPrimaryKey(out_trade_no);
        if (order != null) {
            /**
             * 支付成功修改订单的状态
             */
            order.setState(2);
            orderMapper.updateByPrimaryKeySelective(order);
            BusinessTwo businessTwo = businessTwoMapper.selectByPrimaryKey(order.getPOrder());
            Integer commission = businessTwo.getCommission();
            String userId = order.getUserId();
            User user = userRepository.selectByPrimaryKey(userId);
            MonthOrder monthOrder = new MonthOrder();
            monthOrder.setId(idWorker.nextId() + "");
            monthOrder.setOrderId(out_trade_no);
            monthOrder.setUserId(userId);
            monthOrder.setTime(new Date());
            monthOrder.setState("1");
            monthOrder.setPrice((int) (commission * 0.93));
            monthOrderMapper.insert(monthOrder);
            /**
             * 修改自己的金额
             */
            user.setBalance((int) (user.getBalance() + commission * 0.93));
            userRepository.updateByPrimaryKeySelective(user);

            Integer fwPrice = order.getFwPrice();
            double one = fwPrice * 0.93 * 0.25;
            double two = fwPrice * 0.93 * 0.05;
            User oneUser = userRepository.selectByPrimaryKey(user.getSuperId());
            if (oneUser != null) {
                if (oneUser.getState().equals("2")) {
                    oneUser.setBalance((int) (oneUser.getBalance() + one));
                    userRepository.updateByPrimaryKeySelective(oneUser);
                    monthOrder = new MonthOrder();
                    monthOrder.setId(idWorker.nextId() + "");
                    monthOrder.setOrderId(out_trade_no);
                    monthOrder.setUserId(oneUser.getId());
                    monthOrder.setTime(new Date());
                    monthOrder.setState("1");
                    monthOrder.setPrice((int) one);
                    monthOrderMapper.insert(monthOrder);
                }
                User twoUser = userRepository.selectByPrimaryKey(oneUser.getSuperId());

                if (twoUser != null) {
                    if (twoUser.getState().equals("2")) {
                        twoUser.setBalance((int) (twoUser.getBalance() + two));
                        userRepository.updateByPrimaryKeySelective(twoUser);
                        monthOrder = new MonthOrder();
                        monthOrder.setId(idWorker.nextId() + "");
                        monthOrder.setOrderId(out_trade_no);
                        monthOrder.setUserId(twoUser.getId());
                        monthOrder.setTime(new Date());
                        monthOrder.setState("1");
                        monthOrder.setPrice((int) two);
                        monthOrderMapper.insert(monthOrder);
                    }
                }
            }
        }
    }

    @Autowired
    private VipMapper vipMapper;

    /**
     * 购买vip
     *
     * @param user
     */
    public Map<String, Object> createVip(User user) {
        Vip vip = vipMapper.selectAll().get(0);
        User user1 = userRepository.selectByPrimaryKey(user.getId());
        Integer coupon = user1.getCoupon();
        /**
         * 购物券购买vip
         */
        if (coupon >= vip.getPersonNum()) {
            user.setCoupon(coupon - vip.getPersonNum());
            Calendar c = Calendar.getInstance();
            Map<String, Object> map = new HashMap();
            /**
             * 没有vip
             */
            if (user.getState().equals("1")) {
                VipTime vipTime = new VipTime();
                vipTime.setId(idWorker.nextId() + "");
                vipTime.setJoinTime(new Date());
                vipTime.setUserId(user.getId());
                vipTime.setVipId(vip.getId());
                user.setState("2");
                c.setTime(new Date());
                c.add(Calendar.DATE, 30);
                //当前时间 +30
                vipTime.setExpireTime(c.getTime());
                vipTimeMapper.insert(vipTime);
                User user2 = new User();
                user2.setVipTimeId(vipTime.getId());
                user2.setId(user.getId());
                user2.setState("2");
                user2.setCoupon(vip.getPersonNum() - user1.getCoupon());
                userRepository.updateByPrimaryKeySelective(user2);
                map.put("code", "20002");
                return map;
            }
            /**
             * 有vip
             */
            if (user.getState().equals("2")) {
                String vipTimeId = user.getVipTimeId();
                VipTime vipTime = vipTimeMapper.selectByPrimaryKey(vipTimeId);
                /**
                 * vipTime.getExpireTime() + 30 天
                 */
                c.setTime(vipTime.getExpireTime());
                c.add(Calendar.DATE, 30);
                vipTime.setExpireTime(c.getTime());
                vipTimeMapper.updateByPrimaryKeySelective(vipTime);
                User user2 = new User();
                user2.setId(user.getId());
                user2.setCoupon(vip.getPersonNum() - user1.getCoupon());
                userRepository.updateByPrimaryKeySelective(user2);
                map.put("code", "20002");
                return map;
            }
        }
        /**
         * 微信 + 购物券 购买vip
         */
        Order order = new Order();
        order.setId(idWorker.nextId() + "");
        order.setPrice(vip.getPrice() * 100 - vip.getCouponPrice() * coupon * 100);
        order.setOpenid(user.getOpenid());
        /**
         * 微信支付
         */
        WXPayOrder wxPayOrder = new WXPayOrder(order);
        Map<String, Object> str = null;
        try {
            str = wxPayOrder.getParameter();
            str.put("code", "20001");
        } catch (Exception e) {
            throw new LyException(ExceptionEnum.INVALID_ORDER_STATUS);
        }
        return str;
    }

    /**
     * 线下支付 创建订单
     *
     * @param user   用户
     * @param bid
     * @param pay    支付金额
     * @param remark 备注
     */
    public void xxPayOrder(User user, String bid, Integer pay, String remark) {
        BusinessTwo business = businessTwoMapper.selectByPrimaryKey(bid);
        HzYw hzYw = hzYwRepository.selectByPrimaryKey(business.getOneId());
        Order order = new Order();
        order.setId(idWorker.nextId() + "");
        order.setTime(new Date());
        order.setRemark(remark);
        order.setUserId(user.getId());
        if ("1".equals(user.getState()))
            order.setFwPrice(hzYw.getHyPrice() + business.getCommission());
        if ("2".equals(user.getState()))
            order.setFwPrice(hzYw.getVipPrice() + business.getCommission());
        order.setGfPrice(hzYw.getGfPrice());
        order.setPrice(pay);
        order.setNumber(business.getNumber());
        HzZc hzZc = hzZcRepository.selectByPrimaryKey(hzYw.getZid());
        order.setTitle(hzZc.getName());
        order.setSbName(hzYw.getName());
        order.setPOrder(bid);
        order.setOpenid(user.getOpenid());
        order.setState(1);
        order.setInvoiceState("1");
        order.setNumber(business.getNumber());
        order.setXxPayStart("1");
        orderMapper.insert(order);
    }
}
