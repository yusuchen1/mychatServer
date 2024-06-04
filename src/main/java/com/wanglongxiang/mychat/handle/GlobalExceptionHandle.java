package com.wanglongxiang.mychat.handle;

import com.wanglongxiang.mychat.Execption.BaseExecption;
import com.wanglongxiang.mychat.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandle {
    @ExceptionHandler({Exception.class})
    public Result h1(BaseExecption be){
        log.info("出错!{}",be.getMessage());
        if(be.getCode() != null){
//            如果自己定义了code可以走这条
            return new Result(be.getMessage(),be.getCode());
        }
        return Result.error(be.getMessage());
    }
}
