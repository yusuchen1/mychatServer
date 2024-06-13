package com.wanglongxiang.mychat.interceptor;

import com.wanglongxiang.mychat.context.BaseContext;
import com.wanglongxiang.mychat.exception.UserException.UserUnLoginException;
import com.wanglongxiang.mychat.common.Code;
import com.wanglongxiang.mychat.common.constant.MessageConstant;
import com.wanglongxiang.mychat.common.constant.UserConstant;
import com.wanglongxiang.mychat.properties.JwtProperties;
import com.wanglongxiang.mychat.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component()
@Slf4j
public class UserInterceptor implements HandlerInterceptor {

    @Autowired
    JwtProperties jwtProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        log.info("请求链接:"+request.getRequestURL().toString());
        request.getRequestURL();
        String token = request.getHeader(jwtProperties.getTokenName());
        try{
            Claims claims = JwtUtils.parseJWT(jwtProperties.getSecretKey(), token);
            Long uid = Long.parseLong(claims.get(UserConstant.UID).toString());
            BaseContext.setContext(uid);
        }catch (Exception e){
            throw new UserUnLoginException(MessageConstant.USERUNLOGIN, Code.UNLOGINERROR);
        }
        return true;
    }
}
