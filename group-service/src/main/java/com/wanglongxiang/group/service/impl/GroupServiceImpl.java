package com.wanglongxiang.group.service.impl;

import com.wanglongxiang.api.client.ChatClient;
import com.wanglongxiang.api.client.UserClient;
import com.wanglongxiang.api.dto.GroupInfoDTO;
import com.wanglongxiang.api.dto.SearchDTO;
import com.wanglongxiang.api.other.GroupListItem;
import com.wanglongxiang.api.vo.ChatVO;
import com.wanglongxiang.api.vo.GroupMVO;
import com.wanglongxiang.api.vo.SearchGroupVO;
import com.wanglongxiang.api.vo.UserChatInfoVO;
import com.wanglongxiang.group.exception.groupException.GroupExistException;
import com.wanglongxiang.group.mapper.GroupMapper;
import com.wanglongxiang.group.mapper.GroupMemberMapper;
import com.wanglongxiang.group.pojo.entity.Group;
import com.wanglongxiang.group.pojo.entity.GroupMember;
import com.wanglongxiang.group.service.GroupService;
import com.wanglongxiang.mychat.common.ResultPage;
import com.wanglongxiang.mychat.common.constant.GroupConstant;
import com.wanglongxiang.mychat.common.constant.MessageConstant;
import com.wanglongxiang.mychat.common.constant.UserConstant;
import com.wanglongxiang.mychat.exception.BaseException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    GroupMapper groupMapper;

    @Autowired
    UserClient userClient;

    @Autowired
    GroupMemberMapper groupMemberMapper;

    @Autowired
    ChatClient chatClient;

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
//        User user = userMapper.selectById(userId);
//        group.setMakeUsername(user.getUsername());
        group.setMakeUid(userId);
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

        List<ChatVO> chatVOS = chatClient.getChatVOSByGid(gid);
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
        boolean b = chatClient.delGroupChatByGid(gid);
        groupMemberMapper.deleteByUidAndGid(userId,gid);
    }

    @Override
    public boolean userIsInGroup(Long userId, Long gid) {
        GroupMember gm = groupMemberMapper.getByGidAndUid(gid, userId);
        return gm != null;
    }

    @Override
    public boolean groupIsExists(Long gid) {
        return groupMapper.getById(gid) != null;
    }

    @Override
    public List<Long> getGidsByUid(Long uid) {
        return groupMemberMapper.getByUid(uid)
                .stream()
                .map(GroupMember::getGroupId)
                .collect(Collectors.toList());
    }

    @Override
    public List<GroupListItem> getGroupListItemsByUid(Long uid) {
        List<GroupListItem> groupList = new ArrayList<>();
        List<GroupMember> groupMembers = groupMemberMapper.getByUid(uid);
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
                List<ChatVO> chatVOS = chatClient.getChatVOSByGid(group.getId());
                groupListItem.setChats(chatVOS);
                groupList.add(groupListItem);
            }
        }
        return groupList;
    }

    // 机器人播报
    private void roboteMessage(Long userId, Long gid, String message) {
        UserChatInfoVO userChatInfo = userClient.getUserChatInfo(userId);
        chatClient.customSave(
                userChatInfo.getNickname()+" "+ message,
                GroupConstant.ROBOTID,
                gid
        );
    }
}
