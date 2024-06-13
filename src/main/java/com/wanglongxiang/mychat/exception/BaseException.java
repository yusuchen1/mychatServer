package com.wanglongxiang.mychat.exception;

public class BaseException extends RuntimeException{
    private Integer code;
    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public BaseException() {

    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
