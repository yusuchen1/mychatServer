package com.wanglongxiang.moment.service;

import com.wanglongxiang.api.dto.EditMomentDTO;
import com.wanglongxiang.api.dto.MomentCommontDTO;
import com.wanglongxiang.api.vo.CommontNicknameVO;
import com.wanglongxiang.api.vo.MomentsVO;
import com.wanglongxiang.moment.pojo.entity.Moments;

import java.util.List;
import java.util.Map;

public interface MomentService {
    void saveMoments(Moments moments);

    void editMoments(EditMomentDTO addMomentDTO);

    void deleteMomentById(Long id);

    List<MomentsVO> getMomentsVOS(Long userId);

    String reverseLike(Long momentId, Long userId);

    CommontNicknameVO saveCommont(Long userId, MomentCommontDTO momentCommontDTO);

    void deleteCommont(Long userId, Long momentsDetailsId);

    void updateNickname(Map<Long, String> map);
}
