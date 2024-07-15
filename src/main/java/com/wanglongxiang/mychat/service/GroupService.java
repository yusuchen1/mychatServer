package com.wanglongxiang.mychat.service;

import com.wanglongxiang.mychat.common.ResultPage;
import com.wanglongxiang.mychat.pojo.dto.GroupInfoDTO;
import com.wanglongxiang.mychat.pojo.dto.SearchDTO;
import com.wanglongxiang.mychat.pojo.entity.Group;
import com.wanglongxiang.mychat.pojo.other.GroupListItem;
import com.wanglongxiang.mychat.pojo.vo.ChatVO;
import com.wanglongxiang.mychat.pojo.vo.GroupMVO;
import com.wanglongxiang.mychat.pojo.vo.SearchGroupVO;

import java.io.IOException;
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
}
