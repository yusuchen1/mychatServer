package com.wanglongxiang.mychat.service.impl;

import com.wanglongxiang.mychat.exception.cronyException.CronyGroupExistException;
import com.wanglongxiang.mychat.mapper.*;
import com.wanglongxiang.mychat.pojo.dto.UpdateCronyInfoDTO;
import com.wanglongxiang.mychat.pojo.entity.*;
import com.wanglongxiang.mychat.pojo.other.CronyGroupList;
import com.wanglongxiang.mychat.pojo.other.CronyListItem;
import com.wanglongxiang.mychat.pojo.vo.AgreeCronyVO;
import com.wanglongxiang.mychat.pojo.vo.ChatVO;
import com.wanglongxiang.mychat.pojo.vo.CronyGroupVO;
import com.wanglongxiang.mychat.pojo.vo.SearchUserVO;
import com.wanglongxiang.mychat.service.CronyService;
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
    GroupMapper groupMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    ChatMapper chatMapper;
    @Autowired
    GroupMemberMapper groupMemberMapper;

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
        User user = userMapper.selectById(cronyAsk.getAskId());
        cronyListItem.setName(user.getNickname());
        cronyListItem.setId(user.getId());
        cronyListItem.setAvatar(user.getAvatar());
        List<Chat> chats = chatMapper.selectBySidAndRid(cronyAsk.getObjId(), cronyAsk.getAskId());
        cronyListItem.setChats(chats.stream().map(chat -> {
            ChatVO chatVO = new ChatVO();
            BeanUtils.copyProperties(chat, chatVO);
            User user1 = userMapper.selectById(chat.getSendUid());
            chatVO.setNickname(user1.getNickname());
            chatVO.setAvatar(user1.getAvatar());
            chatVO.setMe(cronyAsk.getObjId().equals(user1.getId()));
            return chatVO;
        }).collect(Collectors.toList()));
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
        cronyMapper.deleteByUidAndCid(userId,cronyId);
    }

    @Override
    public List<SearchUserVO> getUserByGid(Long userId,Long gid,String key) {
        List<GroupMember> groupMembers = groupMemberMapper.getByGid(gid);
        List<Long> userIds = groupMembers.stream()
                .map(groupMember -> groupMember.getMemberId())
                .collect(Collectors.toList());
        List<User> users = userMapper.selectBatchIds(userIds);

        List<SearchUserVO> searchUserVOS = new ArrayList<>();
        for (User user : users) {
            SearchUserVO searchUserVO = new SearchUserVO();
            Crony crony1 = cronyMapper.selectByUserIdAndCronyId(userId, user.getId());
            Crony crony2 = cronyMapper.selectByUserIdAndCronyId(user.getId(), userId);
//            给searchUserVO赋值
            BeanUtils.copyProperties(user,searchUserVO);
            searchUserVO.setCrony(!(crony1 == null || crony2 == null));
            searchUserVOS.add(searchUserVO);
        }
        return searchUserVOS;
    }

    @Override
    public void refuseCronyAsk(Long cronyAskId) {
        cronyAskMapper.deleteById(cronyAskId);
    }


}
