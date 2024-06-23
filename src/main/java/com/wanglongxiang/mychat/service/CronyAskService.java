package com.wanglongxiang.mychat.service;

import com.wanglongxiang.mychat.pojo.entity.CronyAsk;
import com.wanglongxiang.mychat.pojo.vo.UserMessageVO;

import java.util.List;

public interface CronyAskService {
    public void save(CronyAsk cronyAsk);

    public List<UserMessageVO> getUserMessageVOS(Long userId);

}
