package com.wanglongxiang.chat.service.impl;

import com.wanglongxiang.api.client.CronyClient;
import com.wanglongxiang.api.client.GroupClient;
import com.wanglongxiang.api.client.UserClient;
import com.wanglongxiang.api.other.CronyListItem;
import com.wanglongxiang.api.vo.ChatVO;
import com.wanglongxiang.api.vo.CronyDesAndCGidVO;
import com.wanglongxiang.api.vo.UserChatInfoVO;
import com.wanglongxiang.chat.mapper.ChatMapper;
import com.wanglongxiang.chat.pojo.entity.Chat;
import com.wanglongxiang.chat.service.ChatService;
import com.wanglongxiang.mychat.common.constant.GroupConstant;
import com.wanglongxiang.mychat.common.constant.MessageConstant;
import com.wanglongxiang.mychat.exception.BaseException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    ChatMapper chatMapper;

    @Autowired
    UserClient userClient;

    @Autowired
    CronyClient cronyClient;

    @Autowired
    GroupClient groupClient;


    @Override
    public void save(Chat c) {
//        如果不是机器人发消息，则需要判断
        if(!c.getSendUid().equals(GroupConstant.ROBOTID)){
            Long sendUid = c.getSendUid();
            Long receiveUid = c.getReceiveUid();
            Long groupId = c.getGroupId();
//            如果是用户之间互发消息，判断用户之间是否为好友，不是好友抛出异常
            if(receiveUid != null){
                boolean iscrony = cronyClient.isCrony(sendUid, receiveUid);
                if(!iscrony){
                    throw new BaseException(MessageConstant.ITSNOTCRONY);
                }
            }
//            如果是群聊发消息，判断用户是否在群聊中，不在群聊中抛出异常
            else {

                if(!groupClient.groupIsExists(groupId)){
                    throw new BaseException(MessageConstant.GROUPUNEXIST);
                }
                if(!groupClient.userIsInGroup(sendUid,groupId)){
                    throw new BaseException(MessageConstant.GROUPUNINCLUDE);
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
            UserChatInfoVO userChatInfo = userClient.getUserChatInfo(chatVO.getSendUid());
            chatVO.setAvatar(userChatInfo.getAvatar());
            chatVO.setNickname(userChatInfo.getNickname());
            chatVO.setMe(userChatInfo.getId().equals(uid));
            chatVOS.add(chatVO);
        }
        return chatVOS;
    }

    @Override
    public List<ChatVO> getChatVOSByGid(Long userId,Long gid) {
        List<Chat> chats = chatMapper.selectByGid(gid);
        List<ChatVO> chatVOS = chats.stream().map(chat -> {
            ChatVO chatVO = new ChatVO();
            BeanUtils.copyProperties(chat, chatVO);
            UserChatInfoVO userChatInfo = userClient.getUserChatInfo(chat.getSendUid());
            chatVO.setNickname(userChatInfo.getNickname());
            chatVO.setAvatar(userChatInfo.getAvatar());
            chatVO.setMe(userId.equals(userChatInfo.getId()));
            return chatVO;
        }).collect(Collectors.toList());
        return chatVOS;
    }

    @Override
    public void delGroupChatByGid(Long gid) {
        chatMapper.deleteByGid(gid);
    }

    @Override
    public CronyListItem setChatsForCLI(CronyListItem cli,Long objId,Long askId) {
        List<Chat> chats = chatMapper.selectBySidAndRid(objId, askId);
        cli.setChats(chats.stream().map(chat -> {
            ChatVO chatVO = new ChatVO();
            BeanUtils.copyProperties(chat, chatVO);
            UserChatInfoVO uci = userClient.getUserChatInfo(chat.getSendUid());
            chatVO.setNickname(uci.getNickname());
            chatVO.setAvatar(uci.getAvatar());
            chatVO.setMe(objId.equals(uci.getId()));
            return chatVO;
        }).collect(Collectors.toList()));
        return cli;
    }

    /*
     * 查询聊天记录
     * */
    @Override
    public List<ChatVO> getChatVOS(Long sid, Long rid) {
        UserChatInfoVO user = userClient.getUserChatInfo(sid);
        UserChatInfoVO ruser = userClient.getUserChatInfo(rid);
        CronyDesAndCGidVO cronyDesAndCGidVO = cronyClient.getCronyDesAndCGidVO(sid, rid);

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
                chatVO.setNickname(cronyDesAndCGidVO.getDescription());
                chatVO.setAvatar(ruser.getAvatar());
            }
            chatVOS.add(chatVO);
        }

        System.out.println();
        return chatVOS;
    }

    @Override
    public List<List<ChatVO>> getChatVOSS(Long sid, List<Long> rids) {
        UserChatInfoVO user = userClient.getUserChatInfo(sid);

        List<UserChatInfoVO> ucis = userClient.getUserChatInfos(rids);
        List<CronyDesAndCGidVO> cronyDesAndCGidVOs = cronyClient.getCronyDesAndCGidVOs(sid, rids);

        List<List<ChatVO>> chatVOSs = new ArrayList<>();
        for (int i = 0; i < rids.size(); i++) {
            Long rid = rids.get(i);
            String description = cronyDesAndCGidVOs.stream()
                    .filter(cronyDesAndCGidVO -> cronyDesAndCGidVO.getUserId().equals(rid))
                    .collect(Collectors.toList())
                    .get(0).getDescription();

            String avatar = ucis.stream()
                    .filter(uci -> uci.getId().equals(rid))
                    .collect(Collectors.toList())
                    .get(0).getAvatar();

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
                    chatVO.setNickname(description);
                    chatVO.setAvatar(avatar);
                }
                chatVOS.add(chatVO);
            }
            chatVOSs.add(chatVOS);

            System.out.println();
        }
        return chatVOSs;
    }

    @Override
    public void deleteChat(Long chatId) {
        Chat chat = chatMapper.selectById(chatId);
        LocalDateTime chatTime = chat.getTime();
        LocalDateTime now = LocalDateTime.now();
        if(chatTime.plusMinutes(2).isAfter(now)){
            chatMapper.deleteById(chatId);
        }else {
            throw new BaseException(MessageConstant.REVOKETIMEOUT);
        }
    }
}
