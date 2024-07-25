package com.wanglongxiang.moment.RabbitMQ;

import com.wanglongxiang.moment.service.MomentService;
import com.wanglongxiang.mychat.common.constant.MQConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RabbitListener(queues = MQConstant.QUEUE_MOMENT_UPDATEUSER)
public class DirectReceiver {
    @Autowired
    MomentService momentService;

    @RabbitHandler
    public void process(Map<Long,String> map){
        log.info("MQ接收消息:"+map);
        momentService.updateNickname(map);
    }
}
