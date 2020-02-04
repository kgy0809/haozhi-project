package com.haozhi.item.web.common;

import com.haozhi.common.constants.StatusCode;
import com.haozhi.common.dto.ResultDTO;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author kgy
 * @version 1.0
 * @date 2019/12/26 9:34
 */
@RestControllerAdvice
public class BaseExceptionHandler {

    //只要Controller方法有异常，则会自动执行该方法
    @ExceptionHandler//默认处理所有异常
    public ResultDTO error(Throwable e){
        //记录日志（发邮件、发短信、、、、）
        System.out.println("记录日志：发生了异常");
        e.printStackTrace();
        //返回结果
        return new ResultDTO(false, StatusCode.ERROR,e.getMessage());
    }
}
