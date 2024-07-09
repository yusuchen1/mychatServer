package com.wanglongxiang.group.service;

import com.wanglongxiang.api.dto.GroupInfoDTO;
import com.wanglongxiang.api.dto.SearchDTO;
import com.wanglongxiang.api.other.GroupListItem;
import com.wanglongxiang.api.vo.GroupMVO;
import com.wanglongxiang.api.vo.SearchGroupVO;
import com.wanglongxiang.group.pojo.entity.Group;
import com.wanglongxiang.mychat.common.ResultPage;

import java.util.List;

public interface GroupService {
    public List<Group> getListByUid();

    void makeGroup(Long userId, GroupInfoDTO groupInfoDTO);

    List<Long> getUidsByGid(Long gid);

    ResultPage<SearchGroupVO> searchGroup(SearchDTO searchDTO, Long userId);

    GroupListItem joinGroup(Long userId, Long gid);

    Group getByGid(Long gid);

    List<GroupMVO> getGroupMByUid(Long userId);

    void exitGroup(Long userId, Long groupId);

    void updateGroup(Group group);

    void dissGroup(Long userId,Long gid);

    boolean userIsInGroup(Long userId, Long gid);

    boolean groupIsExists(Long gid);

    List<Long> getGidsByUid(Long uid);

    List<GroupListItem> getGroupListItemsByUid(Long uid);
}
