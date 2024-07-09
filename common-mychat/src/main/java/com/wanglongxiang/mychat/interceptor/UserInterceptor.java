package com.wanglongxiang.mychat.interceptor;

import com.wanglongxiang.mychat.context.BaseContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        System.out.println("request.getRequestURL() = " + request.getRequestURL());
//        System.out.println("request.getMethod() = " + request.getMethod());
        String uid = request.getHeader("uid");
        if(uid != null && !uid.equals("")){
            BaseContext.setContext(Long.parseLong(uid));
        }
        return true;
    }
}
