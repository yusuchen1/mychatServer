package com.wanglongxiang.mychat.utils;

import com.wanglongxiang.mychat.pojo.dto.ChatDTO;
import com.wanglongxiang.mychat.pojo.vo.ChatVO;
import com.wanglongxiang.mychat.pojo.vo.LChatVO;
import com.wanglongxiang.mychat.service.ChatService;
import com.wanglongxiang.mychat.service.GroupService;
import com.wanglongxiang.mychat.webSocket.EchoChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class ChatUtil {
    @Autowired
    GroupService groupService;
    @Autowired
    ChatService chatService;
    @Autowired
    EchoChannel echoChannel;

    public void GroupChatSend(Long gid) throws IOException {
        List<Long> uids = groupService.getUidsByGid(gid);
        for (Long uid : uids) {
            List<ChatVO> chatVOS = chatService.selectGroupChat(uid, gid);
            LChatVO lChatVO = new LChatVO();
            lChatVO.setChats(chatVOS);
            lChatVO.setGid(gid);
            echoChannel.sendClientByUid(lChatVO,uid);
        }
    }

    public void CronyChatSend(Long uid1, Long uid2) throws IOException {
        //        给发送消息者推送新消息
        List<ChatVO> chatVOS1 = chatService.selectChat(uid1, uid2);
        LChatVO lChatVO1 = new LChatVO(uid2,null, chatVOS1);
        echoChannel.sendClientByUid(lChatVO1, uid1);

//        给接受消息者推送新消息
        List<ChatVO> chatVOS2 = chatService.selectChat(uid2, uid1);
        LChatVO lChatVO2 = new LChatVO(uid1,null, chatVOS2);
        echoChannel.sendClientByUid(lChatVO2, uid2);
    }
}
