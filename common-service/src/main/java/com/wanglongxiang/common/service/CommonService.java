package com.wanglongxiang.common.service;

import com.wanglongxiang.api.other.CronyGroupList;
import com.wanglongxiang.api.other.GroupListItem;

import java.util.List;

public interface CommonService {
    public List<GroupListItem> getGroupListItemByUid(Long userId);

    public List<CronyGroupList> getCronyGroupListByUid(Long userId);

    void online(Long userId);

    void offLine(Long userId);
}
