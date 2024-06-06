package com.wanglongxiang.mychat.handle;

import com.wanglongxiang.mychat.Exception.BaseException;
import com.wanglongxiang.mychat.common.Code;
import com.wanglongxiang.mychat.common.Result;
import com.wanglongxiang.mychat.common.constant.MessageConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandle {
    /*
    * 自定义异常，一般为用户操作错误抛出的异常
    * */
    @ExceptionHandler({BaseException.class})
    public Result h1(BaseException be){
        log.info("出错!{}",be.getMessage());
        if(be.getCode() != null){
//            如果自己定义了code可以走这条
            return new Result(be.getMessage(),be.getCode());
        }
        return Result.error(be.getMessage());
    }

    /*
    * 非自定义异常走这里
    * 程序异常，需要程序员排查
    * */
    @ExceptionHandler({Exception.class})
    public Result h2(Exception e){
        return new Result(MessageConstant.SYSTEMERROR, Code.PROGRAMERROR);
    }
}
