package com.wanglongxiang.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTest {
    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void String1Test(){
//        ValueOperations valueOperations = redisTemplate.opsForValue();
//        valueOperations.set("name","zhangsan");
        System.out.println("123");
    }
}
