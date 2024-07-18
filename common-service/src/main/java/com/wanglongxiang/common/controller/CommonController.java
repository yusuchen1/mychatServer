package com.wanglongxiang.common.controller;

import com.wanglongxiang.api.other.CronyGroupList;
import com.wanglongxiang.api.other.GroupListItem;
import com.wanglongxiang.api.vo.CronyGroupAGroupVO;
import com.wanglongxiang.common.service.CommonService;
import com.wanglongxiang.mychat.common.Result;
import com.wanglongxiang.mychat.common.constant.RedisConstant;
import com.wanglongxiang.mychat.context.BaseContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        // 远程调用
        List<CronyGroupList> cronyGroupListByUid = commonService.getCronyGroupListByUid(userId);
        List<GroupListItem> groupListItemByUid = commonService.getGroupListItemByUid(userId);
        CronyGroupAGroupVO cronyGroupAGroupVO = new CronyGroupAGroupVO(groupListItemByUid,cronyGroupListByUid);
        return new Result<>(cronyGroupAGroupVO);
    }

    @GetMapping("/online")
    @ApiOperation("用户上线")
    public void onLine(@RequestParam("userId") Long userId){
        log.info("用户上线,userId:{}",userId);
        commonService.online(userId);
    }

    @GetMapping("/offline")
    @ApiOperation("用户下线")
    public void offLine(@RequestParam("userId") Long userId){
        log.info("用户下线,userId:{}",userId);
        commonService.offLine(userId);
    }
}
