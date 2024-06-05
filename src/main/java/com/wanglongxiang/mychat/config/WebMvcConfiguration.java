package com.wanglongxiang.mychat.config;

import com.wanglongxiang.mychat.interceptor.UserInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {
    @Autowired
    UserInterceptor userInterceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("正在注册拦截器...");
        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/**");
        super.addInterceptors(registry);
    }
}
