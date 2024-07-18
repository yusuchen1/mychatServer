package com.wanglongxiang.common.service.impl;

import com.wanglongxiang.api.client.ChatClient;
import com.wanglongxiang.api.client.CronyClient;
import com.wanglongxiang.api.client.GroupClient;
import com.wanglongxiang.api.client.UserClient;
import com.wanglongxiang.api.other.CronyGroupList;
import com.wanglongxiang.api.other.GroupListItem;
import com.wanglongxiang.api.vo.ChatVO;
import com.wanglongxiang.common.service.CommonService;
import com.wanglongxiang.mychat.common.constant.RedisConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    @Autowired
    RedisTemplate redisTemplate;


    @Override
    public List<GroupListItem> getGroupListItemByUid(Long userId) {
        return groupClient.getGroupListItemsByUid(userId);
    }

    @Override
    public List<CronyGroupList> getCronyGroupListByUid(Long userId) {
        List<CronyGroupList> cronyGroupListsByUid = cronyClient.getCronyGroupListsByUid(userId);
        SetOperations setOperations = redisTemplate.opsForSet();
        Set<Long> members = setOperations.members(RedisConstant.ONLINE);
        if(members != null){
            cronyGroupListsByUid.stream().forEach(item -> {
                item.getCronys().stream().forEach(crony -> {
                    crony.setOnline(members.contains(crony.getId()));
                });
            });
        }
        return cronyGroupListsByUid;
    }

}
