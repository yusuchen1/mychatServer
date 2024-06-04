package com.wanglongxiang.mychat.Execption;

public class BaseExecption extends RuntimeException{
    private Integer code;
    public BaseExecption(String message) {
        super(message);
    }

    public BaseExecption(String message,Integer code) {
        super(message);
        this.code = code;
    }

    public BaseExecption() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
