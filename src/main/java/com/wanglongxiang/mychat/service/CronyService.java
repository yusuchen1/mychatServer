package com.wanglongxiang.mychat.service;


import com.wanglongxiang.mychat.pojo.dto.UpdateCronyInfoDTO;
import com.wanglongxiang.mychat.pojo.other.CronyGroupList;
import com.wanglongxiang.mychat.pojo.vo.AgreeCronyVO;
import com.wanglongxiang.mychat.pojo.vo.SearchUserVO;

import java.util.List;

public interface CronyService {
    CronyGroupList agreeCrony(AgreeCronyVO agreeCronyVO);

    void updateCronyInfo(Long userId, UpdateCronyInfoDTO updateCronyInfoDTO);


    void dropCrony(Long userId, Long cronyId);

    List<SearchUserVO> getUserByGid(Long userId,Long gid,String key);

    void refuseCronyAsk(Long cronyAskId);

    List<Long> getCronyIds(Long uid);
}
