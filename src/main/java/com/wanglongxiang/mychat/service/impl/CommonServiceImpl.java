package com.wanglongxiang.mychat.service.impl;

import com.wanglongxiang.mychat.mapper.*;
import com.wanglongxiang.mychat.pojo.entity.*;
import com.wanglongxiang.mychat.pojo.other.CronyGroupList;
import com.wanglongxiang.mychat.pojo.other.CronyListItem;
import com.wanglongxiang.mychat.pojo.other.GroupListItem;
import com.wanglongxiang.mychat.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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



    @Override
    public List<GroupListItem> getGroupListItemByUid(Long userId) {
        List<GroupListItem> groupList = new ArrayList<>();
        List<GroupMember> groupMembers = groupMemberMapper.getByUid(userId);
        List<Long> groupIds = new ArrayList<>();
        for (GroupMember groupMember : groupMembers) {
            groupIds.add(groupMember.getGroupId());
        }
        if(groupIds.size() != 0){
            List<Group> groups = groupMapper.selectBatchIds(groupIds);
            for (Group group : groups) {
                GroupListItem groupListItem = new GroupListItem();
                groupListItem.setId(group.getId());
                groupListItem.setAvatar(group.getGroupAvater());
                groupListItem.setName(group.getGroupName());
//            TODO 插入群组的最新一条消息
                groupListItem.setNmessage("");
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
//                TODO 插入与该好友的最新一条消息
                cronyListItem.setNmessage("");
                cronyListItemList.add(cronyListItem);
            }
            cronyGroupLists.setCronys(cronyListItemList);
            cronyGroupListss.add(cronyGroupLists);
        }
        return cronyGroupListss;
    }
}
