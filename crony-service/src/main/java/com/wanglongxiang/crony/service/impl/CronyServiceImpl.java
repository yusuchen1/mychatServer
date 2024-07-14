package com.wanglongxiang.crony.service.impl;

import com.wanglongxiang.api.client.ChatClient;
import com.wanglongxiang.api.client.GroupClient;
import com.wanglongxiang.api.client.UserClient;
import com.wanglongxiang.api.dto.UpdateCronyInfoDTO;
import com.wanglongxiang.api.other.CronyGroupList;
import com.wanglongxiang.api.other.CronyListItem;
import com.wanglongxiang.api.vo.*;
import com.wanglongxiang.crony.exception.cronyException.ItsNotCronyException;
import com.wanglongxiang.crony.mapper.CronyAskMapper;
import com.wanglongxiang.crony.mapper.CronyGroupMapper;
import com.wanglongxiang.crony.mapper.CronyMapper;
import com.wanglongxiang.crony.pojo.entity.Crony;
import com.wanglongxiang.crony.pojo.entity.CronyAsk;
import com.wanglongxiang.crony.pojo.entity.CronyGroup;
import com.wanglongxiang.crony.service.CronyService;
import com.wanglongxiang.mychat.common.constant.MessageConstant;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CronyServiceImpl implements CronyService {
    @Autowired
    CronyMapper cronyMapper;
    @Autowired
    CronyAskMapper cronyAskMapper;
    @Autowired
    CronyGroupMapper cronyGroupMapper;
    @Autowired
    GroupClient groupClient;
    @Autowired
    UserClient userClient;
    @Autowired
    ChatClient chatClient;

    @Override
    @Transactional
    public CronyGroupList agreeCrony(AgreeCronyVO agreeCronyVO) {
        Long cronyAskId = agreeCronyVO.getCronyAskId();
        Long cronyGroupId = agreeCronyVO.getCronyGroupId();
        String description = agreeCronyVO.getDescription();
        CronyAsk cronyAsk = cronyAskMapper.selectById(cronyAskId);
        Crony crony1 = new Crony(cronyAsk.getObjId(), cronyAsk.getAskId(), cronyGroupId, description);
        Crony crony2 = new Crony(cronyAsk.getAskId(), cronyAsk.getObjId(), cronyAsk.getAskCronyGroupId(), cronyAsk.getDescription());
        cronyMapper.deleteByUidAndCid(cronyAsk.getObjId(), cronyAsk.getAskId());
        cronyMapper.deleteByUidAndCid(cronyAsk.getAskId(), cronyAsk.getObjId());
        cronyMapper.save(crony1);
        cronyMapper.save(crony2);
        cronyAskMapper.deleteById(cronyAskId);

        /*
         * 返回好友聊天记录
         * */
        CronyGroupList cronyGroupList = new CronyGroupList();
        CronyGroup cronyGroup = cronyGroupMapper.selectById(cronyGroupId);
        cronyGroupList.setGroupName(cronyGroup.getCronyGroupName());
        CronyListItem cronyListItem = new CronyListItem();
        UserChatInfoVO userChatInfo = userClient.getUserChatInfo(cronyAsk.getAskId());
        cronyListItem.setName(userChatInfo.getNickname());
        cronyListItem.setId(userChatInfo.getId());
        cronyListItem.setAvatar(userChatInfo.getAvatar());

        cronyListItem = chatClient.setChatsForCLI(cronyListItem, cronyAsk.getObjId(), cronyAsk.getAskId());
        List<CronyListItem> cronyListItems = new ArrayList<>();
        cronyListItems.add(cronyListItem);
        cronyGroupList.setCronys(cronyListItems);
        return cronyGroupList;
    }

    @Override
    public void updateCronyInfo(Long userId, UpdateCronyInfoDTO updateCronyInfoDTO) {
        cronyMapper.updateCronyInfo(userId,
                updateCronyInfoDTO.getCronyId(),
                updateCronyInfoDTO.getDescription(),
                updateCronyInfoDTO.getCronyGroupId());
    }

    @Override
    public void dropCrony(Long userId, Long cronyId) {
        Crony crony = cronyMapper.selectByUserIdAndCronyId(userId, cronyId);
        if(crony == null){
            throw new ItsNotCronyException(MessageConstant.ITSNOTCRONY2);
        }
        cronyMapper.deleteByUidAndCid(userId, cronyId);
    }

    @Override
    public List<SearchUserVO> getUserByGid(Long userId, Long gid, String key) {
        List<Long> userIds = groupClient.getUidsByGid(gid);
        List<SearchUserVO> searchUserVOS = userClient.getSearchUserVOSByUids(userIds);
        return searchUserVOS;
    }

    @Override
    public void refuseCronyAsk(Long cronyAskId) {
        cronyAskMapper.deleteById(cronyAskId);
    }

    @Override
    public boolean isCrony(Long userId1, Long userId2) {
        Crony crony1 = cronyMapper.selectByUserIdAndCronyId(userId1, userId2);
        Crony crony2 = cronyMapper.selectByUserIdAndCronyId(userId2, userId1);
//            给searchUserVO赋值
        return !(crony1 == null || crony2 == null);
    }

    @Override
    public List<CronyGroupVO> getCronyGroupVOS(Long uid) {
        //        设置好友分组
        List<CronyGroup> cronyGroups = cronyGroupMapper.findByUid(uid);
//            将cronyGroups转化为cronyGroupVOS
        List<CronyGroupVO> cronyGroupVOS = new ArrayList<>();
        cronyGroups.stream()
                .map(cronyGroup -> new CronyGroupVO(cronyGroup.getId(),cronyGroup.getCronyGroupName()))
                .forEach(cronyGroupVOS::add);
        return cronyGroupVOS;
    }

    @Override
    public CronyDesAndCGidVO getCronyDesAndCGidVO(Long userId, Long cronyId) {
        Crony crony = cronyMapper.selectByUserIdAndCronyId(userId, cronyId);
        if(crony == null){
            return null;
        }
        return new CronyDesAndCGidVO(cronyId,crony.getDescription(),crony.getCronyGroupId());
    }

    @Override
    public List<CronyDesAndCGidVO> getCronyDesAndCGidVOs(Long userId, List<Long> cronyIds) {
        if(cronyIds == null || cronyIds.size() == 0){
            return new ArrayList<>();
        }
        List<Crony> cronies = cronyMapper.selectByUserIdAndCronyIds(userId, cronyIds);
        if(cronies == null){
            return new ArrayList<>();
        }
        return cronies.stream()
                .map(crony -> new CronyDesAndCGidVO(crony.getCronyId(),crony.getDescription(),crony.getCronyGroupId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getCronyIds(Long userId) {
        List<Long> cronyIds = cronyMapper.getCronyIds(userId);
        cronyIds = cronyIds.stream().filter(id -> isCrony(userId,id)).collect(Collectors.toList());
        return cronyIds;
    }


}
