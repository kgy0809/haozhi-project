package com.haozhi.item.service;

import com.github.pagehelper.PageHelper;
import com.haozhi.item.dao.OrderMapper;
import com.haozhi.item.dao.UserRepository;
import com.haozhi.item.dto.NumDto;
import com.haozhi.item.pojo.Order;
import com.haozhi.item.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/4 9:34
 */
@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 查询全部订单
     * @param type  状态 1代付款 2审核中 3已确认 4已退款 0查询全部订单
     * @param page  1
     * @param rows  10
     * @param userId    用户id
     * @return  List<Order>
     */
    public List<Order> allOrder(String type,Integer page,Integer rows,String userId) {
        if ("0".equals(type)){
            PageHelper.startPage(page,rows);
            Example example = new Example(Order.class);
            example.setOrderByClause("time desc");
            example.createCriteria().andEqualTo("userId",userId);
            List<Order> orders = orderMapper.selectByExample(example);
            return orders;
        }else if ("1".equals(type)){
            PageHelper.startPage(page,rows);
            Example example = new Example(Order.class);
            example.setOrderByClause("time desc");
            example.createCriteria().andEqualTo("userId",userId).andEqualTo("state",type);
            List<Order> orders = orderMapper.selectByExample(example);
            return orders;
        }else if ("2".equals(type)){
            PageHelper.startPage(page,rows);
            Example example = new Example(Order.class);
            example.setOrderByClause("time desc");
            example.createCriteria().andEqualTo("userId",userId).andEqualTo("state",type);
            List<Order> orders = orderMapper.selectByExample(example);
            return orders;
        }else if ("3".equals(type)){
            PageHelper.startPage(page,rows);
            Example example = new Example(Order.class);
            example.setOrderByClause("time desc");
            example.createCriteria().andEqualTo("userId",userId).andEqualTo("state",type);
            List<Order> orders = orderMapper.selectByExample(example);
            return orders;
        }else if ("4".equals(type)){
            PageHelper.startPage(page,rows);
            Example example = new Example(Order.class);
            example.setOrderByClause("time desc");
            example.createCriteria().andEqualTo("userId",userId).andEqualTo("state",type );
            List<Order> orders = orderMapper.selectByExample(example);
            for (Order order : orders) {
                order.setDoublePrice(order.getDoubleFwPrice());
            }
            return orders;
        }
        return null;
    }

    public NumDto queryCountNum(String userId) {
        String newDate = new SimpleDateFormat("yyyy-MM").format(new Date());
        NumDto numDto = new NumDto();
        Example wc = new Example(Order.class);
        wc.createCriteria().andEqualTo("userId",userId).andEqualTo("state",3);
        int wcNum = orderMapper.selectCountByExample(wc);
        numDto.setWcNum(wcNum);
        Example all = new Example(Order.class);
        all.createCriteria().andEqualTo("userId",userId);
        int allNum = orderMapper.selectCountByExample(all);
        numDto.setAllNum(allNum);
        Example df = new Example(Order.class);
        df.createCriteria().andEqualTo("userId",userId).andEqualTo("state",1);
        int dfNum = orderMapper.selectCountByExample(df);
        numDto.setDfNum(dfNum);
        Example ds = new Example(Order.class);
        ds.createCriteria().andEqualTo("userId",userId).andEqualTo("state",2);
        int dsNum = orderMapper.selectCountByExample(ds);
        numDto.setDsNum(dsNum);
        Example t = new Example(Order.class);
        t.createCriteria().andEqualTo("userId",userId).andEqualTo("state",4);
        int tNum = orderMapper.selectCountByExample(t);
        numDto.setTNum(tNum);

        Example newXz = new Example(Order.class);
        newXz.createCriteria().andEqualTo("userId",userId).andIn("state", Arrays.asList(2,3)).andLike("time","%"+newDate+"%");
        int newXzNum = orderMapper.selectCountByExample(newXz);
        numDto.setNewXzNum(newXzNum);

        Example newWc = new Example(Order.class);
        newWc.createCriteria().andEqualTo("userId",userId).andEqualTo("state",3).andLike("time","%"+newDate+"%");
        int newWcNum = orderMapper.selectCountByExample(newWc);
        numDto.setNewWcNum(newWcNum);
        return numDto;
    }

    @Autowired
    private UserRepository userRepository;

    public NumDto queryCountNumNew(String userId) {
        String newDate = new SimpleDateFormat("yyyy-MM").format(new Date());
        NumDto numDto = new NumDto();
        Example newWc = new Example(Order.class);
        newWc.createCriteria().andEqualTo("userId",userId).andEqualTo("state",3).andLike("time","%"+newDate+"%");
        int newWcNum = orderMapper.selectCountByExample(newWc);
        numDto.setNewWcNum(newWcNum);
        Example wc = new Example(Order.class);
        wc.createCriteria().andEqualTo("userId",userId).andEqualTo("state",3);
        int wcNum = orderMapper.selectCountByExample(wc);
        User user = new User();
        user.setId(userId);
        user.setMonthNum(newWcNum);
        user.setTotalNum(wcNum);
        userRepository.updateByPrimaryKeySelective(user);
        numDto.setWcNum(wcNum);
        return numDto;
    }

    public void uploadFileUrl(Order order) {
        orderMapper.updateByPrimaryKeySelective(order);
    }

    public Order queryById(String id) {
        return orderMapper.selectByPrimaryKey(id);
    }

    public Order uploadFileUrlXg(String id) {
        return orderMapper.selectByPrimaryKey(id);
    }
}
