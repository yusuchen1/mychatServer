package com.wanglongxiang.mychat.service.impl;

import com.wanglongxiang.mychat.Exception.cronyException.AskExistException;
import com.wanglongxiang.mychat.common.constant.MessageConstant;
import com.wanglongxiang.mychat.mapper.CronyAskMapper;
import com.wanglongxiang.mychat.pojo.entity.CronyAsk;
import com.wanglongxiang.mychat.service.CronyAskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CronyAskServiceImpl implements CronyAskService {
    @Autowired
    CronyAskMapper cronyAskMapper;
    @Override
    public void save(CronyAsk cronyAsk) {
        CronyAsk c = cronyAskMapper.findByAskIdAndObjId(cronyAsk.getAskId(), cronyAsk.getObjId());
//        如果表中已有请求，抛出异常
        if(c != null){
            throw new AskExistException(MessageConstant.ASKEXIST);
        }
        cronyAskMapper.save(cronyAsk);
    }
}
