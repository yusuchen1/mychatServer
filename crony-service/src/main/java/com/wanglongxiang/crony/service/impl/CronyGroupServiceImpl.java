package com.wanglongxiang.crony.service.impl;

import com.wanglongxiang.api.client.ChatClient;
import com.wanglongxiang.api.client.UserClient;
import com.wanglongxiang.api.other.CronyGroupList;
import com.wanglongxiang.api.other.CronyListItem;
import com.wanglongxiang.api.vo.ChatVO;
import com.wanglongxiang.api.vo.CronyGroupVO;
import com.wanglongxiang.api.vo.UserChatInfoVO;
import com.wanglongxiang.crony.exception.cronyException.CronyGroupExistException;
import com.wanglongxiang.crony.exception.cronyException.CronyGroupOnyOneException;
import com.wanglongxiang.crony.exception.cronyException.CronyGroupUnEmptyException;
import com.wanglongxiang.crony.mapper.CronyAskMapper;
import com.wanglongxiang.crony.mapper.CronyGroupMapper;
import com.wanglongxiang.crony.mapper.CronyMapper;
import com.wanglongxiang.crony.pojo.entity.Crony;
import com.wanglongxiang.crony.pojo.entity.CronyAsk;
import com.wanglongxiang.crony.pojo.entity.CronyGroup;
import com.wanglongxiang.crony.service.CronyGroupService;
import com.wanglongxiang.mychat.common.constant.MessageConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CronyGroupServiceImpl implements CronyGroupService {
    @Autowired
    CronyGroupMapper cronyGroupMapper;

    @Autowired
    CronyMapper cronyMapper;

    @Autowired
    CronyAskMapper cronyAskMapper;

    @Autowired
    UserClient userClient;

    @Autowired
    ChatClient chatClient;


    @Override
    public List<CronyGroupVO> getCronyGroupByUid(Long uid) {
        List<CronyGroup> cronyGroups = cronyGroupMapper.findByUid(uid);
        List<CronyGroupVO> cronyGroupVOS = new ArrayList<>();
        for (CronyGroup cronyGroup : cronyGroups) {
            CronyGroupVO cronyGroupVO = new CronyGroupVO();
            cronyGroupVO.setCronyGroupId(cronyGroup.getId());
            cronyGroupVO.setCrongGroupName(cronyGroup.getCronyGroupName());
            cronyGroupVOS.add(cronyGroupVO);
        }
        return cronyGroupVOS;
    }

    /*
    * 删除好友组
    *   如果组中有好友则不允许删除
    *   如果组中无好友且无该组的好友请求则正常
    *   如果组中无好友且有该组的好友请求则连带好友请求一起删除
    *   如果是最后一个好友组，不予删除
    * */
    @Override
    @Transactional
    public void deleteGroup(Long userId, Long groupId) {
        List<Crony> cronies = cronyMapper.findByGroupId(groupId);
//        好友组不为空，提示用户不允许删除
        if(!cronies.isEmpty()){
            throw new CronyGroupUnEmptyException(MessageConstant.CRONYGROUPUNEMPTY);
        }


//        如果是最后一个好友组，不能删除
        if(cronyGroupMapper.findByUid(userId).size() == 1){
            throw new CronyGroupOnyOneException(MessageConstant.CRONYGROUPONLYONE+",不予删除!");
        }
//        查询是否有该好友组的好友请求
        List<CronyAsk> cronyAsks = cronyAskMapper.findByAskCronyGroupId(groupId);
        if(!cronyAsks.isEmpty()){
            List<Long> ids = new ArrayList<>();
            for (CronyAsk cronyAsk : cronyAsks) {
                ids.add(cronyAsk.getId());
            }
//            删除这些好友请求
            cronyAskMapper.deleteBatchIds(ids);
        }
//        删除好友组
        cronyGroupMapper.deleteById(groupId);
    }

    @Override
    public List<CronyGroup> getCronyGroup(Long userId) {
        return cronyGroupMapper.findByUid(userId);
    }

    @Override
    public List<CronyGroupList> getCronyGroupListsByUid(Long uid) {
        List<CronyGroupList> cronyGroupListss = new ArrayList<>();
        List<CronyGroup> cronyGroups = cronyGroupMapper.findByUid(uid);
        for (CronyGroup cronyGroup : cronyGroups) {
            CronyGroupList cronyGroupLists = new CronyGroupList();
//            设置好友分组名
            cronyGroupLists.setGroupName(cronyGroup.getCronyGroupName());
            List<CronyListItem> cronyListItemList = new ArrayList<>();
//            获取好友组中的所有好友
            Long gid = cronyGroup.getId();
            List<Crony> cs = cronyMapper.findByGroupId(gid);

            List<Long> cids = cs.stream().map(Crony::getCronyId).collect(Collectors.toList());
            List<UserChatInfoVO> ucis = userClient.getUserChatInfos(cids);
            List<List<ChatVO>> chatVOSS = chatClient.getChatVOSS(
                    uid,
                    ucis.stream().map(UserChatInfoVO::getId).collect(Collectors.toList())
            );
            for (int i = 0; i < cs.size(); i++) {
                int index = i;
                UserChatInfoVO uci = ucis
                        .stream()
                        .filter(u -> u.getId().equals(cs.get(index).getCronyId()))
                        .collect(Collectors.toList()).get(0);
                CronyListItem cronyListItem = new CronyListItem();
                cronyListItem.setAvatar(uci.getAvatar());
                cronyListItem.setId(uci.getId());
                cronyListItem.setName(cs.get(i).getDescription());

                cronyListItem.setChats(getChatVOS(chatVOSS,cs.get(index).getCronyId()));
                cronyListItemList.add(cronyListItem);
            }
            cronyGroupLists.setCronys(cronyListItemList);
            cronyGroupListss.add(cronyGroupLists);
        }
        return cronyGroupListss;
    }

    private List<ChatVO> getChatVOS(List<List<ChatVO>> chatVOSS, Long cronyId) {
        for (List<ChatVO> chatVOS : chatVOSS) {
            if(chatVOS.size() != 0 &&
                    (chatVOS.get(0).getSendUid().equals(cronyId) || chatVOS.get(0).getReceiveUid().equals(cronyId))){
                return chatVOS;
            }
        }
        return new ArrayList<>();
    }

    @Override
    public void save(Long userId,String cronyGroupName) {
        CronyGroup c = cronyGroupMapper.findByUidAndGroupName(userId, cronyGroupName);
        if(c != null){
            throw new CronyGroupExistException(MessageConstant.CRONYGROUPEXIST);
        }
        CronyGroup cronyGroup = new CronyGroup();
        cronyGroup.setUserId(userId);
        cronyGroup.setCronyGroupName(cronyGroupName);
        cronyGroupMapper.save(cronyGroup);
    }
}
