package com.wanglongxiang.mychat.service.impl;

import com.wanglongxiang.mychat.exception.cronyException.CronyGroupExistException;
import com.wanglongxiang.mychat.common.constant.MessageConstant;
import com.wanglongxiang.mychat.exception.cronyException.CronyGroupOnyOneException;
import com.wanglongxiang.mychat.exception.cronyException.CronyGroupUnEmptyException;
import com.wanglongxiang.mychat.mapper.CronyAskMapper;
import com.wanglongxiang.mychat.mapper.CronyGroupMapper;
import com.wanglongxiang.mychat.mapper.CronyMapper;
import com.wanglongxiang.mychat.pojo.entity.Crony;
import com.wanglongxiang.mychat.pojo.entity.CronyAsk;
import com.wanglongxiang.mychat.pojo.entity.CronyGroup;
import com.wanglongxiang.mychat.pojo.vo.CronyGroupVO;
import com.wanglongxiang.mychat.service.CronyGroupService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CronyGroupServiceImpl implements CronyGroupService {
    @Autowired
    CronyGroupMapper cronyGroupMapper;

    @Autowired
    CronyMapper cronyMapper;

    @Autowired
    CronyAskMapper cronyAskMapper;


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
