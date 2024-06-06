package com.wanglongxiang.mychat.service;

import com.wanglongxiang.mychat.pojo.other.CronyGroupList;
import com.wanglongxiang.mychat.pojo.other.GroupListItem;

import java.util.List;

public interface CommonService {
    public List<GroupListItem> getGroupListItemByUid(Long userId);

    public List<CronyGroupList> getCronyGroupListByUid(Long userId);
}
