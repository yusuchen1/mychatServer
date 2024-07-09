package com.wanglongxiang.crony.service;

import com.wanglongxiang.api.other.CronyGroupList;
import com.wanglongxiang.api.vo.CronyGroupVO;
import com.wanglongxiang.crony.pojo.entity.CronyGroup;

import java.util.List;


public interface CronyGroupService {
    void save(Long userId,String cronyGroupName);

    List<CronyGroupVO> getCronyGroupByUid(Long uid);

    void deleteGroup(Long userId, Long groupId);

    List<CronyGroup> getCronyGroup(Long userId);

    List<CronyGroupList> getCronyGroupListsByUid(Long uid);
}
