package com.wanglongxiang.mychat.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {
    String message;
    T data;
    Integer code;

    public Result(String message, T data, Integer code) {
        this.message = message;
        this.data = data;
        this.code = code;
    }

    public Result() {
        this.code = Code.SUCESS;
    }

    public Result(String message) {
        this.message = message;
        this.code = Code.SUCESS;
    }



    public Result(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public Result(Integer code) {
        this.code = code;
    }

    public static Result success(){
        return new Result();
    }

    public static Result success(String message){
        return new Result(message);
    }

    public static Result error(){
        return new Result(Code.ERROR);
    }

    public static Result error(String message){
        return new Result(message,Code.ERROR);
    }
}
