package com.wanglongxiang.crony.service;



import com.wanglongxiang.api.dto.UpdateCronyInfoDTO;
import com.wanglongxiang.api.other.CronyGroupList;
import com.wanglongxiang.api.vo.AgreeCronyVO;
import com.wanglongxiang.api.vo.CronyDesAndCGidVO;
import com.wanglongxiang.api.vo.CronyGroupVO;
import com.wanglongxiang.api.vo.SearchUserVO;

import java.util.List;

public interface CronyService {
    CronyGroupList agreeCrony(AgreeCronyVO agreeCronyVO);

    void updateCronyInfo(Long userId, UpdateCronyInfoDTO updateCronyInfoDTO);


    void dropCrony(Long userId, Long cronyId);

    List<SearchUserVO> getUserByGid(Long userId, Long gid, String key);

    void refuseCronyAsk(Long cronyAskId);

    boolean isCrony(Long userId1, Long userId2);

    List<CronyGroupVO> getCronyGroupVOS(Long uid);

    CronyDesAndCGidVO getCronyDesAndCGidVO(Long userId, Long cronyId);

    List<CronyDesAndCGidVO> getCronyDesAndCGidVOs(Long userId, List<Long> cronyIds);
}
