package com.wanglongxiang.mychat;

import com.wanglongxiang.mychat.pojo.entity.Chat;
import com.wanglongxiang.mychat.pojo.vo.ChatVO;
import com.wanglongxiang.mychat.service.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ChatTest {
    @Autowired
    ChatService chatService;

    /*
    * 测试插入消息
    * */
    @Test
    public void saveTest(){
        Chat chat = new Chat();
        chat.setSendUid(2L);
        chat.setReceiveUid(3L);
        chat.setContent("这是一条测试消息!");
        chatService.save(chat);
    }

    /*
    * 测试查询聊天记录
    * */
    @Test
    public void selectChatTest(){
        Long sid = 2L;
        Long rid = 3L;
        List<ChatVO> chats = chatService.selectChat(sid, rid);
        System.out.println(chats);
    }
}
