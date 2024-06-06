package com.wanglongxiang.mychat.service.impl;

import com.wanglongxiang.mychat.mapper.CronyAskMapper;
import com.wanglongxiang.mychat.mapper.CronyMapper;
import com.wanglongxiang.mychat.pojo.entity.Crony;
import com.wanglongxiang.mychat.pojo.entity.CronyAsk;
import com.wanglongxiang.mychat.service.CronyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CronyServiceImpl implements CronyService {
    @Autowired
    CronyMapper cronyMapper;
    @Autowired
    CronyAskMapper cronyAskMapper;

    @Override
    @Transactional
    public void cronyAdd(Long cronyAskId, Long cronyGroupId,String description) {
        CronyAsk cronyAsk = cronyAskMapper.selectById(cronyAskId);
        Crony crony1 = new Crony(cronyAsk.getObjId(), cronyAsk.getAskId(), cronyGroupId, description);
        Crony crony2 = new Crony(cronyAsk.getAskId(), cronyAsk.getObjId(), cronyAsk.getAskCronyGroupId(), cronyAsk.getDescription());
        cronyMapper.save(crony1);
        cronyMapper.save(crony2);
        cronyAskMapper.deleteById(cronyAskId);
    }
}
