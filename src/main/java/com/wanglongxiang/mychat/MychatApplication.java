package com.wanglongxiang.mychat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableCaching
public class MychatApplication {
    public static void main(String[] args) {
        SpringApplication.run(MychatApplication.class, args);
    }

}
