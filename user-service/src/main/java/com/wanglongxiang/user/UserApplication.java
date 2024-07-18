package com.wanglongxiang.user;

import com.wanglongxiang.api.config.DefaultFeignConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan("com.wanglongxiang.user.mapper")
@EnableFeignClients(basePackages = "com.wanglongxiang.api.client",defaultConfiguration = DefaultFeignConfig.class)
@EnableCaching
@SpringBootApplication
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class,args);
    }
}
