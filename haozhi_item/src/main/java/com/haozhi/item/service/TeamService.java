package com.haozhi.item.service;

import com.github.pagehelper.PageHelper;
import com.haozhi.item.dao.MonthOrderMapper;
import com.haozhi.item.dao.OrderMapper;
import com.haozhi.item.dao.UserRepository;
import com.haozhi.item.dto.NewDto;
import com.haozhi.item.pojo.MonthOrder;
import com.haozhi.item.pojo.Order;
import com.haozhi.item.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/2 14:50
 */
@Service
public class TeamService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MonthOrderMapper monthOrderMapper;

    @Autowired
    private OrderMapper orderMapper;

    public List<User> searchList(String type, String name, String date1, String date2, String oneId, Integer page, Integer rows) {
        if ("1".equals(type)) {
            if ((name == null || "".equals(name)) && (date1 == null || "".equals(date1))) {
                Example example = new Example(User.class);
                example.createCriteria().andEqualTo("superId", oneId);
                PageHelper.startPage(page, rows);
                List<User> users = userRepository.selectByExample(example);
                Integer priceAll = 0;
                for (User user : users) {
                    Example ord = new Example(MonthOrder.class);
                    ord.createCriteria().andEqualTo("userId", oneId).andEqualTo("orderState", user.getId());
                    List<MonthOrder> monthOrders = monthOrderMapper.selectByExample(ord);
                    if (monthOrders.size() != 0) {
                        for (MonthOrder monthOrder : monthOrders) {
                            Integer price = monthOrder.getPrice();
                            priceAll += price;
                        }
                    }
                    user.setBalance(priceAll);
                    priceAll = 0;
                }

                return users;
            }
           /* Example ex = new Example(User.class);
            ex.createCriteria().andEqualTo("superId", oneId);
            *//* ex.createCriteria().andEqualTo("time",date).andEqualTo("name",name);*//*
            ex.and().orEqualTo("time", date1).orEqualTo("name", name);*/
            Map<String, Object> map = new HashMap<>();
            map.put("oneId", oneId);
            map.put("name", name);
            map.put("date1", date1);
            map.put("date2", date2);
            List<User> byNameAndAge = userRepository.findByNameAndAge(map);
            Integer priceAll = 0;
            for (User user : byNameAndAge) {
                Example ord = new Example(MonthOrder.class);
                ord.createCriteria().andEqualTo("userId", oneId).andEqualTo("orderState", user.getId());
                List<MonthOrder> monthOrders = monthOrderMapper.selectByExample(ord);
                if (monthOrders.size() != 0) {
                    for (MonthOrder monthOrder : monthOrders) {
                        Integer price = monthOrder.getPrice();
                        priceAll += price;
                    }
                }
                user.setBalance(priceAll);
                priceAll = 0;
            }
            return byNameAndAge;
        } else {
            if ((name == null || "".equals(name)) && (date1 == null || "".equals(date1))) {
                Example example = new Example(User.class);
                example.createCriteria().andEqualTo("superId", oneId);
                /**
                 * 当前用户下所有一级的user
                 */
                List<User> users = userRepository.selectByExample(example);
                List list = new ArrayList();
                users.forEach(user -> {
                    Example ea = new Example(User.class);
                    ea.createCriteria().andEqualTo("superId", user.getId());
                    PageHelper.startPage(page, rows);
                    List<User> select = userRepository.selectByExample(ea);
                    Integer priceAll = 0;
                    for (User user1 : select) {
                        Example ord = new Example(MonthOrder.class);
                        ord.createCriteria().andEqualTo("userId", oneId).andEqualTo("orderState", user1.getId());
                        List<MonthOrder> monthOrders = monthOrderMapper.selectByExample(ord);
                        if (monthOrders.size() != 0) {
                            for (MonthOrder monthOrder : monthOrders) {
                                Integer price = monthOrder.getPrice();
                                priceAll += price;
                            }
                        }
                        user1.setBalance(priceAll);
                        priceAll = 0;
                    }
                    list.add(select);
                });

                return list;
            }
            Example example = new Example(User.class);
            example.createCriteria().andEqualTo("superId", oneId);
            /**
             * 当前用户下所有一级的user
             */
            List<User> users = userRepository.selectByExample(example);
            List listData = new ArrayList();
            Map<String, Object> map = new HashMap<>();
            users.forEach(user -> {
                map.put("oneId", user.getId());
                map.put("name", name);
                map.put("date1", date1);
                map.put("date2", date2);
                List<User> byNameAndAge = userRepository.findByNameAndAge(map);
                Integer priceAll = 0;
                for (User user1 : byNameAndAge) {
                    Example ord = new Example(MonthOrder.class);
                    ord.createCriteria().andEqualTo("userId", oneId).andEqualTo("orderState", user1.getId());
                    List<MonthOrder> monthOrders = monthOrderMapper.selectByExample(ord);
                    if (monthOrders.size() != 0) {
                        for (MonthOrder monthOrder : monthOrders) {
                            Integer price = monthOrder.getPrice();
                            priceAll += price;
                        }
                    }
                    user1.setBalance(priceAll);
                    priceAll = 0;
                }
                listData.add(byNameAndAge);
            });
            return listData;
        }
    }

    /**
     * 团队总人数，当月总业绩
     *
     * @param id
     */
    public NewDto queryNumPrice(String id) {
        /**
         * 当月的总业绩  newPrice
         */
        AtomicReference<Integer> newPrice = new AtomicReference<>(0);
        /**
         * 当月的 日期
         */
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        String newDate = simpleDateFormat.format(date);
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("superId", id);
        /**
         * 一级的总数
         */
        List<User> users = userRepository.selectByExample(example);
        List<Integer> listNum = new ArrayList();
        users.forEach(user -> {
            Example orderEx = new Example(Order.class);
            orderEx.createCriteria().andEqualTo("userId", user.getId()).andEqualTo("state", 3).andLike("time", "%" + newDate + "%");
            List<Order> orders = orderMapper.selectByExample(orderEx);
            orders.forEach(order -> {
                newPrice.updateAndGet(v -> v + order.getPrice());
            });
            Example ea = new Example(User.class);
            ea.createCriteria().andEqualTo("superId", user.getId());
            List<User> users1 = userRepository.selectByExample(ea);
            users1.forEach(user1 -> {
                Example orderEx2 = new Example(Order.class);
                orderEx2.createCriteria().andEqualTo("userId", user1.getId()).andEqualTo("state", 3).andLike("time", "%" + newDate + "%");
                List<Order> orders1 = orderMapper.selectByExample(orderEx2);
                orders1.forEach(order -> {
                    newPrice.updateAndGet(v -> v + order.getPrice());
                });
            });
            Integer i = users1.size();
            listNum.add(i);
        });
        /**
         * 团队的总人数  zsNum
         */
        Integer zsNum = users.size();
        for (Integer o : listNum) {
            zsNum += o;
        }
        NewDto newDto = new NewDto();
        newDto.setNewNum(zsNum);
        newDto.setNewPrice(newPrice.get());
        return newDto;
    }
}
