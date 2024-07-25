package com.wanglongxiang.moment.config;

import com.rabbitmq.client.AMQP;
import com.wanglongxiang.mychat.common.constant.MQConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfiguration {

    @Bean
    Queue queue(){
        return new Queue(MQConstant.QUEUE_MOMENT_UPDATEUSER);
    }

    @Bean
    DirectExchange directExchange(){
        return new DirectExchange(MQConstant.EXCHANGE_UPDATEUSER);
    }

    @Bean
    Binding binding(){
        return BindingBuilder.bind(queue()).to(directExchange()).with(MQConstant.KEY_UPDATEUSER);
    }
}
