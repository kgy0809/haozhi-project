package com.haozhi.greenroom.service;

import com.haozhi.common.utils.IdWorker;
import com.haozhi.greenroom.dao.BusinessTwoMapper;
import com.haozhi.greenroom.dao.HzUserMapper;
import com.haozhi.greenroom.dao.MonthOrderMapper;
import com.haozhi.greenroom.dao.OrderMapper;
import com.haozhi.greenroom.pojo.BusinessTwo;
import com.haozhi.greenroom.pojo.HzUser;
import com.haozhi.greenroom.pojo.MonthOrder;
import com.haozhi.greenroom.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/12 16:00
 */
@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private BusinessTwoMapper businessTwoMapper;
    @Autowired
    private HzUserMapper userRepository;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private MonthOrderMapper monthOrderMapper;

    public List<Order> queryOrder() {
        Example example = new Example(Order.class);
        example.createCriteria().andEqualTo("xxPayStart", "1");
        example.and().andEqualTo("state", "1").orEqualTo("state", "2");
        return orderMapper.selectByExample(example);
    }

    public Order queryOrderById(String id) {
        return orderMapper.selectByPrimaryKey(id);
    }

    public List<Order> queryWxOrder() {
        Example example = new Example(Order.class);
        example.createCriteria().andEqualTo("xxPayStart", "2");
        example.and().andEqualTo("state", "2");
        return orderMapper.selectByExample(example);
    }

    /**
     * 已确认
     *
     * @param id
     */
    public void wxUpdate(String id) {
        Order order = new Order();
        order.setId(id);
        order.setState(3);
        orderMapper.updateByPrimaryKeySelective(order);
    }

    public List<Order> queryYshOrder() {
        Example example = new Example(Order.class);
        example.createCriteria().andEqualTo("state", "3");
        return orderMapper.selectByExample(example);
    }

    public List<Order> queryYtkOrder() {
        Example example = new Example(Order.class);
        example.createCriteria().andEqualTo("state", "4");
        return orderMapper.selectByExample(example);
    }

    /**
     * 退款操作 根据id
     *
     * @param id
     */
    public void tkUpdate(String id) {
        Order order = orderMapper.selectByPrimaryKey(id);
        if (order != null) {
            /**
             * 支付成功修改订单的状态
             */
            order.setState(4);
            orderMapper.updateByPrimaryKeySelective(order);
            BusinessTwo businessTwo = businessTwoMapper.selectByPrimaryKey(order.getPOrder());
            Integer commission = businessTwo.getCommission();
            String userId = order.getUserId();
            HzUser user = userRepository.selectByPrimaryKey(userId);

            /**
             * 删除自己的创建的 完成订单
             */
            Example example = new Example(MonthOrder.class);
            example.createCriteria().andEqualTo("orderId",id);
            example.createCriteria().andEqualTo("userId",userId);
            example.createCriteria().andEqualTo("state","1");
            monthOrderMapper.deleteByExample(example);

            /**
             * 修改自己的金额
             */
            user.setBalance((int) (user.getBalance() - commission * 0.93));
            userRepository.updateByPrimaryKeySelective(user);

            Integer fwPrice = order.getFwPrice();
            double one = fwPrice * 0.93 * 0.25;
            double two = fwPrice * 0.93 * 0.05;
            HzUser oneUser = userRepository.selectByPrimaryKey(user.getSuperId());
            if (oneUser != null) {
                if (oneUser.getState().equals("2")) {
                    oneUser.setBalance((int) (oneUser.getBalance() - one));

                    userRepository.updateByPrimaryKeySelective(oneUser);

                    example = new Example(MonthOrder.class);
                    example.createCriteria().andEqualTo("orderId",id);
                    example.createCriteria().andEqualTo("userId",oneUser.getId());
                    example.createCriteria().andEqualTo("state","1");
                    monthOrderMapper.deleteByExample(example);
                }

                HzUser twoUser = userRepository.selectByPrimaryKey(oneUser.getSuperId());
                if (twoUser != null) {
                    if (twoUser.getState().equals("2")) {
                        twoUser.setBalance((int) (twoUser.getBalance() - two));

                        userRepository.updateByPrimaryKeySelective(twoUser);

                        example = new Example(MonthOrder.class);
                        example.createCriteria().andEqualTo("orderId",id);
                        example.createCriteria().andEqualTo("userId",twoUser.getId());
                        example.createCriteria().andEqualTo("state","1");
                        monthOrderMapper.deleteByExample(example);
                    }
                }
            }
        }
    }

    public void updateOrder(String id) {
        Order order = orderMapper.selectByPrimaryKey(id);
        if (order != null) {
            /**
             * 支付成功修改订单的状态
             */
            order.setState(3);
            orderMapper.updateByPrimaryKeySelective(order);
            BusinessTwo businessTwo = businessTwoMapper.selectByPrimaryKey(order.getPOrder());
            Integer commission = businessTwo.getCommission();
            String userId = order.getUserId();
            HzUser user = userRepository.selectByPrimaryKey(userId);
            MonthOrder monthOrder = new MonthOrder();
            monthOrder.setId(idWorker.nextId() + "");
            monthOrder.setOrderId(id);
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
            HzUser oneUser = userRepository.selectByPrimaryKey(user.getSuperId());
            if (oneUser != null) {
                if (oneUser.getState().equals("2")) {
                    oneUser.setBalance((int) (oneUser.getBalance() + one));
                    userRepository.updateByPrimaryKeySelective(oneUser);
                    monthOrder = new MonthOrder();
                    monthOrder.setId(idWorker.nextId() + "");
                    monthOrder.setOrderId(id);
                    monthOrder.setUserId(oneUser.getId());
                    monthOrder.setTime(new Date());
                    monthOrder.setState("1");
                    monthOrder.setPrice((int) one);
                    monthOrderMapper.insert(monthOrder);
                }
                HzUser twoUser = userRepository.selectByPrimaryKey(oneUser.getSuperId());
                if (twoUser != null) {
                    if (twoUser.getState().equals("2")) {
                        twoUser.setBalance((int) (twoUser.getBalance() + two));
                        userRepository.updateByPrimaryKeySelective(twoUser);
                        monthOrder = new MonthOrder();
                        monthOrder.setId(idWorker.nextId() + "");
                        monthOrder.setOrderId(id);
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
}