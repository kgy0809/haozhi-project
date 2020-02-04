package com.haozhi.greenroom.service;

import com.haozhi.common.utils.IdWorker;
import com.haozhi.greenroom.dao.DataMapper;
import com.haozhi.greenroom.pojo.DataInof;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author kgy
 * @version 1.0
 * @date 2020/1/12 12:30
 */
@Service
public class DataService {

    @Autowired
    private DataMapper dataMapper;
    @Autowired
    private IdWorker idWorker;

    public List<DataInof> queryData() {
        return dataMapper.selectAll();
    }

    public DataInof queryDataById(String id) {
        return dataMapper.selectByPrimaryKey(id);
    }

    public void updateInfo(DataInof dataInof) {
        String sTime = dataInof.getSTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date parse = simpleDateFormat.parse(sTime);
            dataInof.setTime(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dataMapper.updateByPrimaryKey(dataInof);
    }

    public void dataUpdateDelete(String pid) {
        dataMapper.deleteByPrimaryKey(pid);
    }

    /**
     * 新增
     * @param dataInof
     */
    public void addDataInfo(DataInof dataInof) {
        dataInof.setId(idWorker.nextId() + "");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date parse = simpleDateFormat.parse(dataInof.getSTime());
            dataInof.setTime(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dataMapper.insert(dataInof);
    }
}
