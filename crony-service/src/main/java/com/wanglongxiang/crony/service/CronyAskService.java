package com.wanglongxiang.crony.service;

import com.wanglongxiang.api.vo.UserMessageVO;
import com.wanglongxiang.crony.pojo.entity.CronyAsk;

import java.util.List;

public interface CronyAskService {
    public void save(CronyAsk cronyAsk);

    public List<UserMessageVO> getUserMessageVOS(Long userId);

}
