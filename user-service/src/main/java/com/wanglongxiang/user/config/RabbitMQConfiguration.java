package com.wanglongxiang.user.config;

import com.wanglongxiang.mychat.common.constant.MQConstant;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {
    @Bean
    DirectExchange directExchange(){
        return new DirectExchange(MQConstant.EXCHANGE_UPDATEUSER);
    }
}
