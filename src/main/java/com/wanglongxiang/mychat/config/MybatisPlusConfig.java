package com.wanglongxiang.mychat.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@MapperScan("com.wanglongxiang.mychat.mapper")
public class MybatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mpInterceptor(){
        MybatisPlusInterceptor MPi = new MybatisPlusInterceptor();
        MPi.addInnerInterceptor(new PaginationInnerInterceptor());
//        添加乐观锁拦截器
        MPi.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return MPi;
    }
}
