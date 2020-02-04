package com.haozhi.item.service;

import com.github.pagehelper.PageHelper;
import com.haozhi.item.dao.DataRepository;
import com.haozhi.item.dao.DataTextDtoRepository;
import com.haozhi.item.dto.DataTextDto;
import com.haozhi.item.pojo.DataText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kgy
 * @version 1.0
 * @date 2019/12/26 11:10
 */
@Service
public class DataService {

    @Autowired
    private DataRepository dataRepository;
    @Autowired
    private DataTextDtoRepository dataTextDtoRepository;

    /**
     * 查询所有的资料
     * @return
     */
    public List<DataTextDto> textList(Integer page, Integer size) {
        //构建请求的分页对象
        //开启分页助手,传入当前页及每页行数
        PageHelper.startPage(page,size);
        List<DataTextDto> dataTextDtos = dataTextDtoRepository.selectAll();
        return dataTextDtos;
    }

    /**
     * 根据id查询一条详情
     * @param id
     * @return
     */
    public DataText textDetails(String id) {
        return dataRepository.selectByPrimaryKey(id);
    }
}
