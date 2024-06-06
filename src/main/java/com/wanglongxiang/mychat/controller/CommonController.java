package com.wanglongxiang.mychat.controller;

import com.wanglongxiang.mychat.Context.BaseContext;
import com.wanglongxiang.mychat.common.Result;
import com.wanglongxiang.mychat.pojo.other.CronyGroupList;
import com.wanglongxiang.mychat.pojo.other.GroupListItem;
import com.wanglongxiang.mychat.pojo.vo.CronyGroupAGroupVO;
import com.wanglongxiang.mychat.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Autowired
    CommonService commonService;

    @GetMapping("/leftMenu")
    public Result<CronyGroupAGroupVO> getLeftMenu(){
        Long userId = BaseContext.getContext();
        log.info("正在获取左侧列表数据,用户id:{}",userId);
        List<CronyGroupList> cronyGroupListByUid = commonService.getCronyGroupListByUid(userId);
        List<GroupListItem> groupListItemByUid = commonService.getGroupListItemByUid(userId);
        CronyGroupAGroupVO cronyGroupAGroupVO = new CronyGroupAGroupVO(groupListItemByUid,cronyGroupListByUid);
        return new Result<>(cronyGroupAGroupVO);
    }
}
