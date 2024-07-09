package com.wanglongxiang.chat.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/*
* 时间插入切面，自动生成时间
* */
@Aspect
@Component
@Slf4j
public class timeSetAOP {
    @Pointcut("execution(* com.wanglongxiang.chat.service.impl.ChatServiceImpl.save(*))")
    public void pt(){}

    @Before("pt()")
    public void timeSet(JoinPoint joinPoint) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object obj = joinPoint.getArgs()[0];
        log.info("正在为对象插入时间...:{}",obj.toString());
        Class<?> clz = obj.getClass();
        Method setTime = clz.getMethod("setTime", LocalDateTime.class);
        LocalDateTime now = LocalDateTime.now();
        setTime.invoke(obj,now);
    }
}
