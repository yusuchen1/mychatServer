package com.wanglongxiang.crony.service.impl;

import com.wanglongxiang.api.client.UserClient;
import com.wanglongxiang.api.vo.UserChatInfoVO;
import com.wanglongxiang.api.vo.UserMessageVO;
import com.wanglongxiang.crony.exception.cronyException.AskExistException;
import com.wanglongxiang.crony.mapper.CronyAskMapper;
import com.wanglongxiang.crony.pojo.entity.CronyAsk;
import com.wanglongxiang.crony.service.CronyAskService;
import com.wanglongxiang.mychat.common.constant.MessageConstant;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CronyAskServiceImpl implements CronyAskService {
    @Autowired
    CronyAskMapper cronyAskMapper;

    @Autowired
    UserClient userClient;

    @Override
    @Transactional
    public void save(CronyAsk cronyAsk) {
        CronyAsk c = cronyAskMapper.findByAskIdAndObjId(cronyAsk.getAskId(), cronyAsk.getObjId());
//        如果表中已有请求，抛出异常
        if(c != null){
            throw new AskExistException(MessageConstant.ASKEXIST);
        }
        CronyAsk c2 = cronyAskMapper.findByAskIdAndObjId(cronyAsk.getObjId(),cronyAsk.getAskId());
        if(c2 != null){
            throw new AskExistException(MessageConstant.ASKDONTAGREE);
        }
        cronyAskMapper.save(cronyAsk);
    }

    @Override
    public List<UserMessageVO> getUserMessageVOS(Long userId) {
        List<CronyAsk> cronyAsks = cronyAskMapper.findByObjId(userId);
        List<UserMessageVO> userMessageVOS = new ArrayList<>();
        for (CronyAsk cronyAsk : cronyAsks) {
            UserMessageVO userMessageVO = new UserMessageVO();
            UserChatInfoVO userChatInfo = userClient.getUserChatInfo(cronyAsk.getAskId());
            BeanUtils.copyProperties(userChatInfo,userMessageVO);
//            把请求id放入到消息中
            userMessageVO.setAskId(cronyAsk.getId());
            userMessageVOS.add(userMessageVO);
        }
        return userMessageVOS;
    }
}
