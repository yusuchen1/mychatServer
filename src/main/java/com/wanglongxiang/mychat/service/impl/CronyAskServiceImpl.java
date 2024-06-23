package com.wanglongxiang.mychat.service.impl;

import com.wanglongxiang.mychat.exception.cronyException.AskExistException;
import com.wanglongxiang.mychat.common.constant.MessageConstant;
import com.wanglongxiang.mychat.mapper.CronyAskMapper;
import com.wanglongxiang.mychat.mapper.UserMapper;
import com.wanglongxiang.mychat.pojo.entity.CronyAsk;
import com.wanglongxiang.mychat.pojo.entity.User;
import com.wanglongxiang.mychat.pojo.vo.UserMessageVO;
import com.wanglongxiang.mychat.service.CronyAskService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CronyAskServiceImpl implements CronyAskService {
    @Autowired
    CronyAskMapper cronyAskMapper;

    @Autowired
    UserMapper userMapper;

    @Override
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
            User user = userMapper.selectById(cronyAsk.getAskId());
            BeanUtils.copyProperties(user,userMessageVO);
//            把请求id放入到消息中
            userMessageVO.setAskId(cronyAsk.getId());
            userMessageVOS.add(userMessageVO);
        }
        return userMessageVOS;
    }
}
