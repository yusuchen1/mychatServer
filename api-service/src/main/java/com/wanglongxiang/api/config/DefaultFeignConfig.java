package com.wanglongxiang.api.config;

import com.wanglongxiang.mychat.context.BaseContext;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;

public class DefaultFeignConfig {
    @Bean
    public Logger.Level feignLogLevel(){
        System.out.println("正在注册OpenFeign日志级别...");
        return Logger.Level.FULL;
    }

    //添加请求头
    @Bean
    public RequestInterceptor userInfoRequestInterceptor(){
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                Long userInfo = BaseContext.getContext();
                if(userInfo != null){
                    requestTemplate.header("uid", userInfo.toString());
                }
            }
        };
    }
}
