package com.wanglongxiang.mychat.service.impl;

import com.wanglongxiang.mychat.mapper.ChatMapper;
import com.wanglongxiang.mychat.mapper.UserMapper;
import com.wanglongxiang.mychat.pojo.entity.Chat;
import com.wanglongxiang.mychat.pojo.entity.User;
import com.wanglongxiang.mychat.pojo.vo.ChatVO;
import com.wanglongxiang.mychat.service.ChatService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    ChatMapper chatMapper;

    @Autowired
    UserMapper userMapper;

    @Override
    public void save(Chat c) {
        chatMapper.insert(c);
    }

    @Override
    public List<ChatVO> selectChat(Long sid, Long rid) {

        List<ChatVO> chatVOS = getChatVOS(sid, rid);
        return chatVOS;
    }

    private List<ChatVO> getChatVOS(Long sid, Long rid) {
        User user = userMapper.selectById(sid);
        User ruser = userMapper.selectById(rid);

        List<Chat> chats = chatMapper.selectBySidAndRid(sid, rid);
        List<Chat> chats2 = chatMapper.selectBySidAndRid(rid, sid);
        chats.addAll(chats2);
        Collections.sort(chats,((o1, o2) -> {
            return o1.getTime().compareTo(o2.getTime());
        }));
        List<ChatVO> chatVOS = new ArrayList<>();
        for (Chat chat : chats) {
            ChatVO chatVO = new ChatVO();
            BeanUtils.copyProperties(chat,chatVO);
            Long sendUid = chat.getSendUid();
            if(sendUid.equals(sid)){
                chatVO.setMe(true);
                chatVO.setNickname(user.getNickname());
                chatVO.setAvatar(user.getAvatar());
            }else {
                chatVO.setMe(false);
                chatVO.setNickname(ruser.getNickname());
                chatVO.setAvatar(ruser.getAvatar());
            }
            chatVOS.add(chatVO);
        }

        System.out.println();
        return chatVOS;
    }
}
