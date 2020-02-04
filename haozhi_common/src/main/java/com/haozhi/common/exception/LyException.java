package com.haozhi.common.exception;

import com.haozhi.common.enums.ExceptionEnum;
import lombok.Getter;

/**
 * 自定义异常
 */
@Getter
public class LyException extends RuntimeException {

    private int status;  //自定义异常的状态码

    public LyException(ExceptionEnum em) {
        super(em.getMessage());
        this.status = em.getStatus();
    }

    /**
     * 增加异常的构造方法，可以接受String错误信息的字符串
     * @param em
     */
    public LyException(ExceptionEnum em, String msg) {
        super(msg);
        this.status = em.getStatus();
    }
}
