package com.wanglongxiang.common;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Set;

@SpringBootTest
public class RedisTest {
    @Autowired
    RedisTemplate redisTemplate;
    @Test
    public void StringTest(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("name","zhangsan");
    }

    @Test
    public void SetTest(){
        SetOperations setOperations = redisTemplate.opsForSet();
        setOperations.add("online1","id1","1","id2","0","id3","1");
        Set online = setOperations.members("online1");
//        setOperations.add("online","id1","0","id4","1","id3","0");
    }

    @Test
    public void hashTest(){
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.put("online","zhangsan",1);
        hashOperations.put("online","lisi",1);
        hashOperations.put("online","lisi",0);
        Object o = hashOperations.get("online", "zhangsan");
        System.out.println(o);
    }
}
