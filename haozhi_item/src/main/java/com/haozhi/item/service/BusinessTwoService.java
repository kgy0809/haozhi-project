package com.haozhi.item.service;

import com.haozhi.item.dao.BusinessTwoMapper;
import com.haozhi.item.dao.HzYwRepository;
import com.haozhi.item.dao.HzZcRepository;
import com.haozhi.item.dao.OrderMapper;
import com.haozhi.item.pojo.BusinessTwo;
import com.haozhi.item.pojo.HzYw;
import com.haozhi.item.pojo.HzZc;
import com.haozhi.item.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/13 15:56
 */
@Service
public class BusinessTwoService {
    @Autowired
    private BusinessTwoMapper businessTwoMapper;

    @Autowired
    private HzYwRepository hzYwRepository;

    @Autowired
    private HzZcRepository hzZcRepository;

    @Autowired
    private OrderMapper orderMapper;

    public String mycan(String id) {
        Order order = orderMapper.selectByPrimaryKey(id);
        BusinessTwo businessTwo = businessTwoMapper.selectByPrimaryKey(order.getPOrder());
        HzYw hzYw = hzYwRepository.selectByPrimaryKey(businessTwo.getOneId());
        if (hzYw.getId().equals("10013")){
            return null;
        }
        HzZc hzZc = hzZcRepository.selectByPrimaryKey(hzYw.getZid());
        if (hzZc.getId().equals("10086")){
            return "10086";
        }else if (hzZc.getId().equals("10087")){
            return "10087";
        }else if (hzZc.getId().equals("10088")){
            return "10088";
        }else if (hzZc.getId().equals("10089")){
            return "10089";
        }else if (hzZc.getId().equals("10090")){
            return "10090";
        }else if (hzZc.getId().equals("10091")){
            return "10091";
        }
        return null;
    }

    public void deleteById(String id) {
        Order order = orderMapper.selectByPrimaryKey(id);
        orderMapper.deleteByPrimaryKey(id);
        businessTwoMapper.deleteByPrimaryKey(order.getPOrder());
    }

    public String mycanPay(String id) {
        Order order = orderMapper.selectByPrimaryKey(id);
        BusinessTwo businessTwo = businessTwoMapper.selectByPrimaryKey(order.getPOrder());
        HzYw hzYw = hzYwRepository.selectByPrimaryKey(businessTwo.getOneId());
        HzZc hzZc = hzZcRepository.selectByPrimaryKey(hzYw.getZid());
        if (hzZc.getId().equals("10086")){
            return "10086";
        }else if (hzZc.getId().equals("10087")){
            return "10087";
        }else if (hzZc.getId().equals("10088")){
            return "10088";
        }else if (hzZc.getId().equals("10089")){
            return "10089";
        }else if (hzZc.getId().equals("10090")){
            return "10090";
        }else if (hzZc.getId().equals("10091")){
            return "10091";
        }
        return null;
    }
}
