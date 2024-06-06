package com.wanglongxiang.mychat.service.impl;

import com.wanglongxiang.mychat.Exception.cronyException.CronyGroupExistException;
import com.wanglongxiang.mychat.common.constant.MessageConstant;
import com.wanglongxiang.mychat.mapper.CronyGroupMapper;
import com.wanglongxiang.mychat.pojo.entity.CronyGroup;
import com.wanglongxiang.mychat.service.CronyGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CronyGroupServiceImpl implements CronyGroupService {
    @Autowired
    CronyGroupMapper cronyGroupMapper;
    @Override
    public void save(Long userId,String cronyGroupName) {
        CronyGroup c = cronyGroupMapper.findByUidAndGroupName(userId, cronyGroupName);
        if(c != null){
            throw new CronyGroupExistException(MessageConstant.CRONYGROUPEXIST);
        }
        CronyGroup cronyGroup = new CronyGroup();
        cronyGroup.setUserId(userId);
        cronyGroup.setCronyGroupName(cronyGroupName);
        cronyGroupMapper.save(cronyGroup);
    }
}
