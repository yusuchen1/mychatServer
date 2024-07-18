package com.wanglongxiang.mychat.service.impl;

import com.wanglongxiang.mychat.common.constant.RedisConstant;
import com.wanglongxiang.mychat.mapper.*;
import com.wanglongxiang.mychat.pojo.entity.*;
import com.wanglongxiang.mychat.pojo.other.CronyGroupList;
import com.wanglongxiang.mychat.pojo.other.CronyListItem;
import com.wanglongxiang.mychat.pojo.other.GroupListItem;
import com.wanglongxiang.mychat.pojo.vo.ChatVO;
import com.wanglongxiang.mychat.service.ChatService;
import com.wanglongxiang.mychat.service.CommonService;
import com.wanglongxiang.mychat.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    GroupMapper groupMapper;

    @Autowired
    GroupMemberMapper groupMemberMapper;

    @Autowired
    CronyGroupMapper cronyGroupMapper;

    @Autowired
    CronyMapper cronyMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    ChatMapper chatMapper;

    @Autowired
    RedisTemplate redisTemplate;




    @Override
    public List<GroupListItem> getGroupListItemByUid(Long userId) {
        List<GroupListItem> groupList = new ArrayList<>();
        List<GroupMember> groupMembers = groupMemberMapper.getByUid(userId);
        List<Long> groupIds = new ArrayList<>();
        for (GroupMember groupMember : groupMembers) {
            groupIds.add(groupMember.getGroupId());
        }
        if(groupIds.size() != 0){
            List<Group> groups = groupMapper.getByIds(groupIds);
            for (Group group : groups) {
                GroupListItem groupListItem = new GroupListItem();
                groupListItem.setId(group.getId());
                groupListItem.setAvatar(group.getAvatar());
                groupListItem.setName(group.getName());
//            TODO 插入群组的消息集合
                List<Chat> chats = chatMapper.selectByGid(group.getId());
                List<ChatVO> chatVOS = new ArrayList<>();
                for (Chat chat : chats) {
                    ChatVO chatVO = new ChatVO();
                    BeanUtils.copyProperties(chat,chatVO);
                    User user = userMapper.selectById(chatVO.getSendUid());
                    chatVO.setAvatar(user.getAvatar());
                    chatVO.setNickname(user.getNickname());
                    chatVO.setMe(user.getId().equals(userId));
                    chatVOS.add(chatVO);
                }
                groupListItem.setChats(chatVOS);
                groupList.add(groupListItem);
            }
        }
        return groupList;
    }

    @Override
    public List<CronyGroupList> getCronyGroupListByUid(Long userId) {
        List<CronyGroupList> cronyGroupListss = new ArrayList<>();
        List<CronyGroup> cronyGroups = cronyGroupMapper.findByUid(userId);
        for (CronyGroup cronyGroup : cronyGroups) {
            CronyGroupList cronyGroupLists = new CronyGroupList();
//            设置好友分组名
            cronyGroupLists.setGroupName(cronyGroup.getCronyGroupName());
            List<Crony> cronies = cronyMapper.findByGroupId(cronyGroup.getId());
            List<CronyListItem> cronyListItemList = new ArrayList<>();
//            获取好友组中的所有好友
            Long gid = cronyGroup.getId();
            List<Crony> cs = cronyMapper.findByGroupId(gid);
            for (Crony c : cs) {
                User user = userMapper.selectById(c.getCronyId());
                CronyListItem cronyListItem = new CronyListItem();
                cronyListItem.setAvatar(user.getAvatar());
                cronyListItem.setId(user.getId());
                cronyListItem.setName(c.getDescription());
                cronyListItem.setChats(getChatVOS(userId,user.getId()));
                cronyListItemList.add(cronyListItem);
            }
            cronyGroupLists.setCronys(cronyListItemList);
            cronyGroupListss.add(cronyGroupLists);
        }
        SetOperations setOperations = redisTemplate.opsForSet();
        Set<Long> members = setOperations.members(RedisConstant.ONLINE);
        if(members != null){
            cronyGroupListss.stream().forEach(item -> {
                item.getCronys().stream().forEach(crony -> {
                    crony.setOnline(members.contains(crony.getId()));
                });
            });
        }
        return cronyGroupListss;
    }

    /*
    * 查询聊天记录
    * */
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
//            如果是自己
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

    @Override
    public void onLine(Long userId){
        log.info("用户上线,userId:{}",userId);
        SetOperations setOperations = redisTemplate.opsForSet();
        setOperations.add(RedisConstant.ONLINE,userId);
    }

    @Override
    public void offLine(Long userId){
        log.info("用户下线,userId:{}",userId);
        SetOperations setOperations = redisTemplate.opsForSet();
        setOperations.remove(RedisConstant.ONLINE,userId);
    }
}
