package com.wanglongxiang.mychat.service;

import com.wanglongxiang.mychat.pojo.dto.dto.EditMomentDTO;
import com.wanglongxiang.mychat.pojo.dto.dto.MomentCommontDTO;
import com.wanglongxiang.mychat.pojo.entity.Moments;
import com.wanglongxiang.mychat.pojo.vo.CommontNicknameVO;
import com.wanglongxiang.mychat.pojo.vo.MomentsVO;

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
