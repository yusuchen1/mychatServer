package com.wanglongxiang.mychat.service.impl;

import com.wanglongxiang.mychat.common.ResultPage;
import com.wanglongxiang.mychat.common.constant.GroupConstant;
import com.wanglongxiang.mychat.common.constant.MessageConstant;
import com.wanglongxiang.mychat.common.constant.UserConstant;
import com.wanglongxiang.mychat.exception.BaseException;
import com.wanglongxiang.mychat.exception.groupException.GroupExistException;
import com.wanglongxiang.mychat.mapper.ChatMapper;
import com.wanglongxiang.mychat.mapper.GroupMapper;
import com.wanglongxiang.mychat.mapper.GroupMemberMapper;
import com.wanglongxiang.mychat.mapper.UserMapper;
import com.wanglongxiang.mychat.pojo.dto.GroupInfoDTO;
import com.wanglongxiang.mychat.pojo.dto.SearchDTO;
import com.wanglongxiang.mychat.pojo.entity.Chat;
import com.wanglongxiang.mychat.pojo.entity.Group;
import com.wanglongxiang.mychat.pojo.entity.GroupMember;
import com.wanglongxiang.mychat.pojo.entity.User;
import com.wanglongxiang.mychat.pojo.other.GroupListItem;
import com.wanglongxiang.mychat.pojo.vo.ChatVO;
import com.wanglongxiang.mychat.pojo.vo.GroupMVO;
import com.wanglongxiang.mychat.pojo.vo.SearchGroupVO;
import com.wanglongxiang.mychat.service.GroupService;
import com.wanglongxiang.mychat.utils.ChatUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.MacSpi;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    GroupMapper groupMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    GroupMemberMapper groupMemberMapper;

    @Autowired
    ChatMapper chatMapper;

    @Override
    public List<Group> getListByUid() {
        return null;
    }

    /*
    * 创建群聊
    * 设置群聊人数为1，然后将创建者放入到群聊成员表中
    * */
    @Override
    @Transactional
    public void makeGroup(Long userId, GroupInfoDTO groupInfoDTO) {
        Group group = new Group();
        BeanUtils.copyProperties(groupInfoDTO,group);
//        如果用户没有上传头像，则设置为默认头像
        if(group.getAvatar() == null ||group.getAvatar().equals("")){
            group.setAvatar(UserConstant.defaultAvatar);
        }
        User user = userMapper.selectById(userId);
        group.setMakeUid(userId);
        group.setMakeUsername(user.getUsername());
        group.setNum(1);
        Group g = groupMapper.findByNumber(groupInfoDTO.getNumber());
        if(g != null){
            throw new GroupExistException(MessageConstant.GROUPEXIST);
        }
        groupMapper.save(group);

//        将创建者放入群组成员表
        GroupMember groupMember = new GroupMember();
        groupMember.setGroupId(group.getId());
        groupMember.setMemberId(userId);
        groupMemberMapper.save(groupMember);
    }

    @Override
    public List<Long> getUidsByGid(Long gid) {
        List<GroupMember> groupMembers = groupMemberMapper.getByGid(gid);
        return groupMembers
                .stream()
                .map(item -> item.getMemberId())
                .collect(Collectors.toList());
    }

    @Override
    public ResultPage<SearchGroupVO> searchGroup(SearchDTO searchDTO, Long userId) {
        ResultPage<SearchGroupVO> resultPage = new ResultPage<>();
//        获取到查询条件
        String key = searchDTO.getKey();
        Integer page = searchDTO.getPage();
        Integer size = searchDTO.getSize();
        resultPage.setTotal(groupMapper.searchCountByNameOrNumber(key,page,size));
        List<Group> groups = groupMapper.searchByNameOrNumber(key, page, size);
        List<SearchGroupVO> searchGroupVOS = groups.stream().map(item -> {
            List<GroupMember> groupMembers = groupMemberMapper.getByGid(item.getId());
//            获取群中所有用户id
            List<Long> uids = groupMembers.stream()
                    .map(item1 -> item1.getMemberId())
                    .collect(Collectors.toList());
            SearchGroupVO searchGroupVO = new SearchGroupVO();
            BeanUtils.copyProperties(item, searchGroupVO);
            searchGroupVO.setInclude(uids.contains(userId));
            return searchGroupVO;
        }).collect(Collectors.toList());
        resultPage.setData(searchGroupVOS);

        return resultPage;
    }

    @Override
    @Transactional
    public GroupListItem joinGroup(Long userId, Long gid){
        GroupMember groupMember = new GroupMember();
        groupMember.setGroupId(gid);
        groupMember.setMemberId(userId);
        groupMemberMapper.save(groupMember);
        roboteMessage(userId, gid, GroupConstant.JOINMESSAGE);


        List<Chat> chats = chatMapper.selectByGid(gid);
//        if(chats == null){
//            chats = new ArrayList<>();
//        }
        List<ChatVO> chatVOS = chats.stream().map(chat -> {
            ChatVO chatVO = new ChatVO();
            BeanUtils.copyProperties(chat, chatVO);
            User user = userMapper.selectById(chat.getSendUid());
            chatVO.setNickname(user.getNickname());
            chatVO.setAvatar(user.getAvatar());
            chatVO.setMe(userId.equals(user.getId()));
            return chatVO;
        }).collect(Collectors.toList());
        Group group = groupMapper.getById(gid);
        GroupListItem groupListItem = new GroupListItem();
        BeanUtils.copyProperties(group,groupListItem);
        groupListItem.setChats(chatVOS);

//        群人数加一
        groupMapper.updateNum(gid,group.getNum()+1);

        return groupListItem;
    }

    @Override
    public Group getByGid(Long gid) {
        Group group = groupMapper.getById(gid);
        if(group == null){
            throw new BaseException(MessageConstant.GROUPUNEXIST);
        }
        return group;
    }

    @Override
    public List<GroupMVO> getGroupMByUid(Long userId) {
        List<Group> groups = groupMapper.getByUid(userId);
        List<GroupMVO> groupMVOS = groups.stream()
                .map(item -> new GroupMVO(item.getId(), item.getAvatar(), item.getName(), item.getNumber()))
                .collect(Collectors.toList());
        return groupMVOS;
    }

    /*
    * 群主不允许退出群聊
    * */
    @Override
    @Transactional
    public void exitGroup(Long userId, Long groupId) {
        Group group = groupMapper.getById(groupId);
//        如果是群主要退出群聊，不允许
        if(group == null){
            groupMemberMapper.deleteByUidAndGid(userId,groupId);
            return;
        }

        if(group.getMakeUid().equals(userId)){
            throw new BaseException(MessageConstant.GROUPDONTEXIT);
        }
        groupMemberMapper.deleteByUidAndGid(userId,groupId);

//        退出群聊成功，机器人播报
        roboteMessage(userId,groupId,GroupConstant.EXITGROUPO);
//        群聊人数减一
        groupMapper.updateNum(groupId,group.getNum()-1);
    }

    @Override
    public void updateGroup(Group group) {
        Group g1 = groupMapper.findByNumber(group.getNumber());
        if(g1 != null && !g1.getId().equals(group.getId())){
            throw new GroupExistException(MessageConstant.GROUPEXIST);
        }
        groupMapper.updateById(group);
    }

    @Override
    @Transactional
    public void dissGroup(Long userId,Long gid) {
        groupMapper.deleteById(gid);
        groupMemberMapper.deleteByGid(gid);
        chatMapper.deleteByGid(gid);
        groupMemberMapper.deleteByUidAndGid(userId,gid);
    }

    // 机器人播报
    private void roboteMessage(Long userId, Long gid, String message) {
        User p = userMapper.selectById(userId);
        Chat chat1 = new Chat();
        chat1.setContent(p.getNickname()+" "+ message);
        chat1.setSendUid(GroupConstant.ROBOTID);
        chat1.setGroupId(gid);
        chat1.setTime(LocalDateTime.now());
        chatMapper.insert(chat1);
    }
}
