package com.wanglongxiang.user;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MQTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void test1(){
        System.out.println(rabbitTemplate);
    }
}
