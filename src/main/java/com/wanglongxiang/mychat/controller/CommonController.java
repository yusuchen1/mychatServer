package com.wanglongxiang.mychat.controller;

import com.wanglongxiang.mychat.context.BaseContext;
import com.wanglongxiang.mychat.common.Result;
import com.wanglongxiang.mychat.pojo.other.CronyGroupList;
import com.wanglongxiang.mychat.pojo.other.GroupListItem;
import com.wanglongxiang.mychat.pojo.vo.CronyGroupAGroupVO;
import com.wanglongxiang.mychat.service.CommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/common")
@Slf4j
@Api(tags = {"公共接口"})
public class CommonController {

    @Autowired
    CommonService commonService;


    @GetMapping("/leftMenu")
    @ApiOperation("获取主页的左侧菜单")
    public Result<CronyGroupAGroupVO> getLeftMenu(){
        Long userId = BaseContext.getContext();
        log.info("正在获取左侧列表数据,用户id:{}",userId);
        List<CronyGroupList> cronyGroupListByUid = commonService.getCronyGroupListByUid(userId);
        List<GroupListItem> groupListItemByUid = commonService.getGroupListItemByUid(userId);
        CronyGroupAGroupVO cronyGroupAGroupVO = new CronyGroupAGroupVO(groupListItemByUid,cronyGroupListByUid);
        return new Result<>(cronyGroupAGroupVO);
    }
}
