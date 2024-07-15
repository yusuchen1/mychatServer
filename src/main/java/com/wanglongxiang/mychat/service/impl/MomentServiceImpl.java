package com.wanglongxiang.mychat.service.impl;

import com.wanglongxiang.mychat.common.constant.MessageConstant;
import com.wanglongxiang.mychat.exception.BaseException;
import com.wanglongxiang.mychat.mapper.CronyMapper;
import com.wanglongxiang.mychat.mapper.MomentDetailsMapper;
import com.wanglongxiang.mychat.mapper.MomentMapper;
import com.wanglongxiang.mychat.mapper.UserMapper;
import com.wanglongxiang.mychat.pojo.dto.dto.EditMomentDTO;
import com.wanglongxiang.mychat.pojo.dto.dto.MomentCommontDTO;
import com.wanglongxiang.mychat.pojo.entity.Crony;
import com.wanglongxiang.mychat.pojo.entity.Moments;
import com.wanglongxiang.mychat.pojo.entity.MomentsDetails;
import com.wanglongxiang.mychat.pojo.entity.User;
import com.wanglongxiang.mychat.pojo.vo.CommontNicknameVO;
import com.wanglongxiang.mychat.pojo.vo.MomentsCommentsVO;
import com.wanglongxiang.mychat.pojo.vo.MomentsLikeVO;
import com.wanglongxiang.mychat.pojo.vo.MomentsVO;
import com.wanglongxiang.mychat.service.MomentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MomentServiceImpl implements MomentService {
    @Autowired
    MomentMapper momentMapper;
    @Autowired
    MomentDetailsMapper momentDetailsMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    CronyMapper cronyMapper;

    @Override
    public void saveMoments(Moments moments) {
        User user = userMapper.selectById(moments.getUserId());
        moments.setAvatar(user.getAvatar());
        moments.setNickname(user.getNickname());
        moments.setTime(LocalDateTime.now());
        momentMapper.insert(moments);
    }

    @Override
    public void editMoments(EditMomentDTO addMomentDTO) {
        momentMapper.updateContent(addMomentDTO.getId(),addMomentDTO.getContent());
    }

    @Override
    @Transactional
    public void deleteMomentById(Long id) {
        Moments moments = momentMapper.selectById(id);
        if(moments != null){
            momentMapper.deleteById(id);
            momentDetailsMapper.deleteByMomentsId(id);
        }else {
            throw new BaseException(MessageConstant.MomentUNEXIST);
        }
    }

    @Override
    public List<MomentsVO> getMomentsVOS(Long userId) {
        List<Long> cronyIds = getCronyIds(userId);
        cronyIds.add(userId);
        List<MomentsVO> momentsVOList = new ArrayList<>();
        for (Long cronyId : cronyIds) {
            List<Moments> moments = momentMapper.selectByUserId(cronyId);
            List<Long> momentIds = moments.stream().map(Moments::getId).collect(Collectors.toList());
            for (Moments moment : moments) {
                List<MomentsLikeVO> likelist = new ArrayList<>();
                List<MomentsCommentsVO> comments = new ArrayList<>();
                List<MomentsDetails> momentsDetails = momentDetailsMapper.selectByMomentId(moment.getId());
                for (MomentsDetails momentsDetail : momentsDetails) {
                    if (momentsDetail.getContent() == null || momentsDetail.getContent().equals("")){
//                    点赞信息
                        if(momentsDetail.getLike()){
                            MomentsLikeVO momentsLikeVO = new MomentsLikeVO();
                            BeanUtils.copyProperties(momentsDetail,momentsLikeVO);
                            likelist.add(momentsLikeVO);
                        }
                    }else {
//                    评论信息
                        MomentsCommentsVO momentsCommentsVO = new MomentsCommentsVO();
                        BeanUtils.copyProperties(momentsDetail,momentsCommentsVO);
                        comments.add(momentsCommentsVO);
                    }
                }
                MomentsVO momentsVO = new MomentsVO();
                BeanUtils.copyProperties(
                        moment
                        ,momentsVO);
                momentsVO.setLikelist(likelist);
                momentsVO.setComments(comments);
                momentsVO.setMe(momentsVO.getUserId().equals(userId));
                momentsVOList.add(momentsVO);
            }
        }
        momentsVOList.sort(((o1, o2) -> o2.getTime().compareTo(o1.getTime())));
        return momentsVOList;
    }

    public List<Long> getCronyIds(Long userId) {
        List<Long> cronyIds = cronyMapper.getCronyIds(userId);
        cronyIds = cronyIds.stream().filter(id -> isCrony(userId,id)).collect(Collectors.toList());
        return cronyIds;
    }

    public boolean isCrony(Long userId1, Long userId2) {
        Crony crony1 = cronyMapper.selectByUserIdAndCronyId(userId1, userId2);
        Crony crony2 = cronyMapper.selectByUserIdAndCronyId(userId2, userId1);
//            给searchUserVO赋值
        return !(crony1 == null || crony2 == null);
    }

    @Override
    public String reverseLike(Long momentId, Long userId) {
        List<MomentsDetails> momentsDetails = momentDetailsMapper.selectByMomentId(momentId);
        List<MomentsDetails> collect = momentsDetails.stream()
                .filter(momentsDetail ->
                        (momentsDetail.getContent() == null
                                && momentsDetail.getUserId().equals(userId)))
                .collect(Collectors.toList());
        MomentsDetails md;
        if(collect.size() == 0){
            md = new MomentsDetails();
            md.setUserId(userId);
            md.setLike(true);
            md.setTime(LocalDateTime.now());
            md.setMomentsId(momentId);
            User user = userMapper.selectById(userId);
            md.setNickname(user.getNickname());
            momentDetailsMapper.insert(md);
        }else {
            md = collect.get(0);
            md.setLike(!md.getLike());
            momentDetailsMapper.updateById(md);
        }
        return md.getNickname();
    }

    @Override
    public CommontNicknameVO saveCommont(Long userId, MomentCommontDTO momentCommontDTO) {
        ArrayList<Long> uids = new ArrayList<>();
        Collections.addAll(uids,userId,momentCommontDTO.getReplay());
        List<User> users = userMapper.selectBatchIds(uids);
        MomentsDetails md = new MomentsDetails();
        md.setUserId(userId);
        BeanUtils.copyProperties(momentCommontDTO,md);
        md.setTime(LocalDateTime.now());
        md.setNickname(users.stream()
                .filter(user -> user.getId().equals(userId))
                .collect(Collectors.toList())
                .get(0).
                getNickname());
        if(momentCommontDTO.getReplay() != null){
            md.setReplayNickname(users.stream()
                    .filter(user -> user.getId().equals(momentCommontDTO.getReplay()))
                    .collect(Collectors.toList())
                    .get(0).
                    getNickname());
        }
        momentDetailsMapper.insert(md);
        return new CommontNicknameVO(md.getId(),md.getUserId(),md.getNickname(),md.getReplayNickname());
    }

    @Override
    public void deleteCommont(Long userId, Long momentsDetailsId) {
        MomentsDetails momentsDetails = momentDetailsMapper.selectById(momentsDetailsId);
        if(!momentsDetails.getUserId().equals(userId)){
            throw new BaseException(MessageConstant.DONTDELETEOTHERCOM);
        }
        momentDetailsMapper.deleteById(momentsDetailsId);
    }

    @Override
    @Transactional
    public void updateNickname(Map<Long, String> map) {
        Set<Map.Entry<Long, String>> entries = map.entrySet();
        for (Map.Entry<Long, String> entry : entries) {
            Long id = entry.getKey();
            String nickname = entry.getValue();
            momentMapper.updateNickname(id,nickname);
            momentDetailsMapper.updateNickname(id,nickname);
            momentDetailsMapper.updateReplayNickname(id,nickname);
        }
    }
}
