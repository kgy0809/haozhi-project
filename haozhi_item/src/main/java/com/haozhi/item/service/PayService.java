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

    /**
     * 微信支付
     * @param user
     * @param bid
     * @param pay
     * @param remark
     * @return
     */
    public Map<String, Object> createOrder(User user, String bid, Integer pay, String remark) {
        BusinessTwo business = businessTwoMapper.selectByPrimaryKey(bid);
        HzYw hzYw = hzYwRepository.selectByPrimaryKey(business.getOneId());
        Example example = new Example(Order.class);
        example.createCriteria().andEqualTo("pOrder",bid);
        Order newOrder = orderMapper.selectOneByExample(example);
        if (newOrder != null){
            newOrder.setRemark(remark);
            orderMapper.updateByPrimaryKeySelective(newOrder);
            /**
             * 微信支付
             */
            WXPayOrder wxPayOrder = new WXPayOrder(newOrder);
            Map<String, Object> str = null;
            try {
                str = wxPayOrder.getParameter();
            } catch (Exception e) {
                throw new LyException(ExceptionEnum.INVALID_ORDER_STATUS);
            }
            return str;
        }
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
            monthOrder.setOrderState("2");
            monthOrder.setPrice((int) (commission * 0.94));
            monthOrderMapper.insert(monthOrder);
            /**
             * 修改自己的金额
             */
            user.setBalance((int) (user.getBalance() + commission * 0.94));
            user.setTotalNum(user.getTotalNum() + 1);
            userRepository.updateByPrimaryKeySelective(user);
            HzYw hzYw = hzYwRepository.selectByPrimaryKey(businessTwo.getOneId());

            double one = hzYw.getVipPrice() * order.getNumber() * 0.93 * 0.25;
            double two = hzYw.getVipPrice() * order.getNumber() * 0.93 * 0.05;
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
                    monthOrder.setOrderState(userId);
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
                        monthOrder.setOrderState(userId);
                        monthOrderMapper.insert(monthOrder);
                    }
                }
            }
        }
    }

    @Autowired
    private VipMapper vipMapper;

    @Autowired
    private VipOrderMapper vipOrderMapper;

    /**
     * 购买vip
     *
     * @param user
     */
    public Map<String, Object> createVip(User user) {
        Vip vip = vipMapper.selectAll().get(0);
        user = userRepository.selectByPrimaryKey(user.getId());
        Integer coupon = user.getCoupon();
        VipOrder vipOrder = new VipOrder();
        vipOrder.setId(idWorker.nextId() + "");
        vipOrder.setUserId(user.getId());
        vipOrder.setState("2");
        vipOrder.setOpenid(user.getOpenid());
        vipOrder.setTime(new Date());
        if (coupon >= vip.getPersonNum()) {
            vipOrder.setPrice(vip.getPrice() * 100 - vip.getCouponPrice() * vip.getPersonNum() * 100);
            vipOrder.setCount(vip.getPersonNum());
        } else {
            vipOrder.setCount(coupon);
            vipOrder.setPrice(vip.getPrice() * 100 - vip.getCouponPrice() * coupon * 100);
        }
        vipOrderMapper.insert(vipOrder);
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
                c.setTime(new Date());
                c.add(Calendar.DATE, 30);
                //当前时间 +30
                vipTime.setExpireTime(c.getTime());
                vipTime.setState("1");
                vipTimeMapper.insert(vipTime);
                user.setVipTimeId(vipTime.getId());
                user.setId(user.getId());
                user.setState("2");
                userRepository.updateByPrimaryKeySelective(user);
                vipOrder.setState("1");
                vipOrderMapper.updateByPrimaryKeySelective(vipOrder);
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
                vipTime.setState("1");
                vipTimeMapper.updateByPrimaryKeySelective(vipTime);
                user.setId(user.getId());
                userRepository.updateByPrimaryKeySelective(user);
                vipOrder.setState("1");
                vipOrderMapper.updateByPrimaryKeySelective(vipOrder);
                map.put("code", "20002");
                return map;
            }
        }
        /**
         * 微信 + 购物券 购买vip
         */
        Order order = new Order();
        order.setId(vipOrder.getId());
        order.setPrice(vipOrder.getPrice());
        order.setOpenid(user.getOpenid());
        order.setVipState("10086");
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
        Example example = new Example(Order.class);
        example.createCriteria().andEqualTo("pOrder",bid);
        Order newOrder = orderMapper.selectOneByExample(example);
        if (newOrder != null){
            newOrder.setRemark(remark);
            orderMapper.updateByPrimaryKeySelective(newOrder);
            return;
        }
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

    /**
     * vip订单处理
     *
     * @param out_trade_no
     */
    public void queryOrderVip(String out_trade_no) {
        Vip vip = vipMapper.selectAll().get(0);
        VipOrder vipOrder = vipOrderMapper.selectByPrimaryKey(out_trade_no);
        User user = userRepository.selectByPrimaryKey(vipOrder.getUserId());
        user.setCoupon(user.getCoupon() - vipOrder.getCount());
        /**
         * 没有vip
         */
        if (user.getState().equals("1")) {
            Calendar c = Calendar.getInstance();
            VipTime vipTime = new VipTime();
            vipTime.setId(idWorker.nextId() + "");
            vipTime.setJoinTime(new Date());
            vipTime.setUserId(user.getId());
            vipTime.setVipId(vip.getId());
            vipTime.setState("1");
            c.setTime(new Date());
            c.add(Calendar.DATE, 30);
            //当前时间 +30
            vipTime.setExpireTime(c.getTime());
            vipTime.setState("1");
            vipTimeMapper.insert(vipTime);
            user.setVipTimeId(vipTime.getId());
            user.setId(user.getId());
            user.setState("2");
            userRepository.updateByPrimaryKeySelective(user);
            vipOrder.setState("1");
            vipOrderMapper.updateByPrimaryKeySelective(vipOrder);
            return;
        }
        /**
         * 有vip
         */
        if (user.getState().equals("2")) {
            Calendar c = Calendar.getInstance();
            String vipTimeId = user.getVipTimeId();
            VipTime vipTime = vipTimeMapper.selectByPrimaryKey(vipTimeId);
            /**
             * vipTime.getExpireTime() + 30 天
             */
            c.setTime(vipTime.getExpireTime());
            c.add(Calendar.DATE, 30);
            vipTime.setExpireTime(c.getTime());
            vipTime.setState("1");
            vipTimeMapper.updateByPrimaryKeySelective(vipTime);
            user.setId(user.getId());
            userRepository.updateByPrimaryKeySelective(user);
            vipOrder.setState("1");
            vipOrderMapper.updateByPrimaryKeySelective(vipOrder);
        }
    }
}
