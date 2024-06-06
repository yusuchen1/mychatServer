package com.wanglongxiang.mychat;

import com.wanglongxiang.mychat.pojo.entity.Chat;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EntityTest {
    @Test
    public void chatTest(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
        String formattedTime = now.format(formatter);

//        Chat chat = new Chat(1L, 2L, 3L, 4L, now);
        System.out.println("Formatted time: " + formattedTime);
//        System.out.println("Chat object: " + chat);
    }
}
