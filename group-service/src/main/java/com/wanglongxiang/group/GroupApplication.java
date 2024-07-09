package com.wanglongxiang.group;

import com.wanglongxiang.api.config.DefaultFeignConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan("com.wanglongxiang.group.mapper")
@EnableFeignClients(basePackages = "com.wanglongxiang.api.client",defaultConfiguration = DefaultFeignConfig.class)
@SpringBootApplication
public class GroupApplication {
    public static void main(String[] args) {
        SpringApplication.run(GroupApplication.class,args);
    }
}
