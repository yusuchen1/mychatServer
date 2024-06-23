package com.wanglongxiang.mychat.service.impl;

import com.wanglongxiang.mychat.common.constant.GroupConstant;
import com.wanglongxiang.mychat.common.constant.MessageConstant;
import com.wanglongxiang.mychat.exception.cronyException.ItsNotCronyException;
import com.wanglongxiang.mychat.exception.groupException.GroupUnIncludeException;
import com.wanglongxiang.mychat.mapper.ChatMapper;
import com.wanglongxiang.mychat.mapper.CronyMapper;
import com.wanglongxiang.mychat.mapper.GroupMemberMapper;
import com.wanglongxiang.mychat.mapper.UserMapper;
import com.wanglongxiang.mychat.pojo.entity.Chat;
import com.wanglongxiang.mychat.pojo.entity.Crony;
import com.wanglongxiang.mychat.pojo.entity.GroupMember;
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

    @Autowired
    CronyMapper cronyMapper;

    @Autowired
    GroupMemberMapper groupMemberMapper;

    @Override
    public void save(Chat c) {
//        如果不是机器人发消息，则需要判断
        if(!c.getSendUid().equals(GroupConstant.ROBOTID)){
            Long sendUid = c.getSendUid();
            Long receiveUid = c.getReceiveUid();
            Long groupId = c.getGroupId();
            Crony crony1 = null;
            Crony crony2 = null;
//            如果是用户之间互发消息，判断用户之间是否为好友，不是好友抛出异常
            if(receiveUid != null){
                crony1 = cronyMapper.selectByUserIdAndCronyId(sendUid, receiveUid);
                crony2 = cronyMapper.selectByUserIdAndCronyId(receiveUid, sendUid);
                if(crony1 == null || crony2 == null){
                    throw new ItsNotCronyException(MessageConstant.ITSNOTCRONY);
                }
            }
//            如果是群聊发消息，判断用户是否在群聊中，不在群聊中抛出异常
            else {
                GroupMember groupMember = groupMemberMapper.getByGidAndUid(groupId, sendUid);
                if(groupMember == null){
                    throw new GroupUnIncludeException(MessageConstant.GROUPUNINCLUDE);
                }
            }
            chatMapper.insert(c);
        }

    }

    @Override
    public List<ChatVO> selectChat(Long sid, Long rid) {

        List<ChatVO> chatVOS = getChatVOS(sid, rid);
        return chatVOS;
    }

    @Override
    public List<ChatVO> selectGroupChat(Long uid, Long gid) {
        List<Chat> chats = chatMapper.selectByGid(gid);
        List<ChatVO> chatVOS = new ArrayList<>();
        for (Chat chat : chats) {
            ChatVO chatVO = new ChatVO();
            BeanUtils.copyProperties(chat,chatVO);
            User user = userMapper.selectById(chatVO.getSendUid());
            chatVO.setAvatar(user.getAvatar());
            chatVO.setNickname(user.getNickname());
            chatVO.setMe(user.getId().equals(uid));
            chatVOS.add(chatVO);
        }
        return chatVOS;
    }

    private List<ChatVO> getChatVOS(Long sid, Long rid) {
        User user = userMapper.selectById(sid);
        User ruser = userMapper.selectById(rid);
        Crony crony = cronyMapper.selectByUserIdAndCronyId(sid, rid);

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
//            如果是查询聊天记录的那个人则设置为true
            if(sendUid.equals(sid)){
                chatVO.setMe(true);
                chatVO.setNickname(user.getNickname());
                chatVO.setAvatar(user.getAvatar());
            }else {
                chatVO.setMe(false);
                chatVO.setNickname(crony.getDescription());
                chatVO.setAvatar(ruser.getAvatar());
            }
            chatVOS.add(chatVO);
        }

        System.out.println();
        return chatVOS;
    }
}
