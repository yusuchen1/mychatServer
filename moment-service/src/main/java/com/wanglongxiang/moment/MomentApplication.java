package com.wanglongxiang.moment;

import com.wanglongxiang.api.config.DefaultFeignConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@MapperScan("com.wanglongxiang.moment.mapper")
@EnableFeignClients(basePackages = "com.wanglongxiang.api.client" ,defaultConfiguration = DefaultFeignConfig.class)
@SpringBootApplication
public class MomentApplication {
    public static void main(String[] args) {
        SpringApplication.run(MomentApplication.class,args);
    }
}
