package com.wanglongxiang.group.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/*
 * 为分页查询设置页码
 * */
@Component
@Aspect
@Slf4j
public class pageSetAOP {
    @Pointcut("execution(* com.wanglongxiang.group.service.impl.*Impl.search* (..) )")
    public void pt(){}

    @Before("pt()")
    public void setPage(JoinPoint joinPoint) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        log.info("aop：设置页码");
        Object[] args = joinPoint.getArgs();
        Object p = args[0];
        Class<?> clz = p.getClass();

//        获取页码
        Method getPage = clz.getMethod("getPage");
        Integer page = Integer.parseInt(getPage.invoke(p).toString());

//        获取页面大小
        Method getSize = clz.getMethod("getSize");
        Integer size = Integer.parseInt(getSize.invoke(p).toString());

//        重新设置页码
        Method setPage = clz.getMethod("setPage",Integer.class);
        setPage.invoke(p,(page - 1) * size);
    }
}
