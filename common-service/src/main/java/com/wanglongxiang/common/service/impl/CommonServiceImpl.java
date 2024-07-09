package com.wanglongxiang.common.service.impl;

import com.wanglongxiang.api.client.ChatClient;
import com.wanglongxiang.api.client.CronyClient;
import com.wanglongxiang.api.client.GroupClient;
import com.wanglongxiang.api.client.UserClient;
import com.wanglongxiang.api.other.CronyGroupList;
import com.wanglongxiang.api.other.GroupListItem;
import com.wanglongxiang.api.vo.ChatVO;
import com.wanglongxiang.common.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    GroupClient groupClient;

    @Autowired
    CronyClient cronyClient;

    @Autowired
    UserClient userClient;

    @Autowired
    ChatClient chatClient;


    @Override
    public List<GroupListItem> getGroupListItemByUid(Long userId) {
        return groupClient.getGroupListItemsByUid(userId);
    }

    @Override
    public List<CronyGroupList> getCronyGroupListByUid(Long userId) {
        return cronyClient.getCronyGroupListsByUid(userId);
    }

}
