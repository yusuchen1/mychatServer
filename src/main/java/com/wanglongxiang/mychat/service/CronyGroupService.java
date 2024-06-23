package com.wanglongxiang.mychat.service;

import com.wanglongxiang.mychat.pojo.entity.CronyGroup;
import com.wanglongxiang.mychat.pojo.vo.CronyGroupVO;

import java.util.List;


public interface CronyGroupService {
    void save(Long userId,String cronyGroupName);

    List<CronyGroupVO> getCronyGroupByUid(Long uid);

    void deleteGroup(Long userId, Long groupId);

    List<CronyGroup> getCronyGroup(Long userId);
}
