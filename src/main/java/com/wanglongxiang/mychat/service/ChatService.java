package com.wanglongxiang.mychat.service;

import com.wanglongxiang.mychat.pojo.entity.Chat;
import com.wanglongxiang.mychat.pojo.vo.ChatVO;

import java.util.List;

public interface ChatService {
    public void save(Chat c);

    List<ChatVO> selectChat(Long sid, Long rid);

    List<ChatVO> selectGroupChat(Long uid,Long gid);

    Long deleteChat(Long chatId);
}
