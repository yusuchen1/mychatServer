package com.wanglongxiang.mychat;

import com.wanglongxiang.mychat.properties.JwtProperties;
import com.wanglongxiang.mychat.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class MychatApplicationTests {
    @Autowired
    JwtProperties jwtProperties;
    @Test
    public void test1(){
        System.out.println(jwtProperties);
    }
    @Test
    public void createJwt(){
        Map<String,Object> m = new HashMap<>();
        m.put("username","suchen");
        String jwt = JwtUtils.createJwt(jwtProperties.getSecretKey(), jwtProperties.getTtl(), m);
        System.out.println(jwt);
    }
}
