package com.wanglongxiang.chat.service;


import com.wanglongxiang.api.other.CronyListItem;
import com.wanglongxiang.api.vo.ChatVO;
import com.wanglongxiang.chat.pojo.entity.Chat;

import java.util.List;
import java.util.Map;

public interface ChatService {
    public void save(Chat c);

    List<ChatVO> selectChat(Long sid, Long rid);

    List<ChatVO> selectGroupChat(Long uid,Long gid);

    List<ChatVO> getChatVOSByGid(Long userId,Long gid);

    void delGroupChatByGid(Long gid);

    CronyListItem setChatsForCLI(CronyListItem cli,Long objId,Long askId);

    List<ChatVO> getChatVOS(Long sid, Long rid);

    List<List<ChatVO>> getChatVOSS(Long sid, List<Long> rids);

    Long deleteChat(Long chatId);
}
