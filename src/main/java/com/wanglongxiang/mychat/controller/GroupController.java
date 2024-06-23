package com.wanglongxiang.mychat.controller;

import com.wanglongxiang.mychat.common.Code;
import com.wanglongxiang.mychat.common.Result;
import com.wanglongxiang.mychat.common.ResultPage;
import com.wanglongxiang.mychat.common.constant.GroupConstant;
import com.wanglongxiang.mychat.common.constant.MessageConstant;
import com.wanglongxiang.mychat.context.BaseContext;
import com.wanglongxiang.mychat.pojo.dto.GroupInfoDTO;
import com.wanglongxiang.mychat.pojo.dto.SearchDTO;
import com.wanglongxiang.mychat.pojo.entity.Group;
import com.wanglongxiang.mychat.pojo.other.GroupListItem;
import com.wanglongxiang.mychat.pojo.vo.ChatVO;
import com.wanglongxiang.mychat.pojo.vo.GroupMVO;
import com.wanglongxiang.mychat.pojo.vo.SearchGroupVO;
import com.wanglongxiang.mychat.service.GroupService;
import com.wanglongxiang.mychat.utils.ChatUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.List;

@Api(tags = "群聊相关接口")
@Slf4j
@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    GroupService groupService;
    @Autowired
    ChatUtil chatUtil;

    @PostMapping("/makeGroup")
    @ApiOperation("创建群聊")
    public Result makeGroup(@RequestBody GroupInfoDTO groupInfoDTO){
        Long userId = BaseContext.getContext();
        log.info("{}创建群聊:{}",userId,groupInfoDTO);
        groupService.makeGroup(userId,groupInfoDTO);
        return Result.success(MessageConstant.CREATESUCCESS);
    }

    @GetMapping("/searchGroup")
    @ApiOperation("查询群聊")
    public Result<ResultPage<SearchGroupVO>> searchGroup(SearchDTO searchDTO){
        Long userId = BaseContext.getContext();
        log.info("userId:{}查询群聊:{}",userId,searchDTO);
        ResultPage<SearchGroupVO> resultPage = groupService.searchGroup(searchDTO, userId);
        return  new Result<>(resultPage);
    }

    @PostMapping("/joinGroup")
    @ApiOperation("用户加入群聊")
    public Result<GroupListItem> addGroup(@RequestParam("gid") Long gid) throws IOException {
        Long userId = BaseContext.getContext();
        log.info("用户:{}正在申请加入群聊:{}",userId,gid);
        GroupListItem groupListItem = groupService.joinGroup(userId, gid);
        chatUtil.GroupChatSend(gid);
        return new Result<>(MessageConstant.ADDSUCCESS,groupListItem, Code.SUCESS);
    }

    @GetMapping("/groupInfo")
    @ApiOperation("获取群聊信息")
    public Result<Group> getGroupInfo(Long gid){
        Long userId = BaseContext.getContext();
        log.info("userId:{},正在获取群聊消息,gid:{}",userId,gid);
        Group group = groupService.getByGid(gid);
        return new Result<>(group);
    }

    @GetMapping("/groupsM")
    @ApiOperation("获取群聊管理界面信息")
    public Result<List<GroupMVO>> getGroupsM(){
        Long userId = BaseContext.getContext();
        log.info("userId:{}正在获取他的群聊信息",userId);
        List<GroupMVO> groupMVOS = groupService.getGroupMByUid(userId);
        return new Result<>(groupMVOS);
    }

    @DeleteMapping("/exitGroup")
    @ApiOperation("退出群聊")
    public Result exitGroup(@PathParam("gid") Long gid) throws IOException {
        Long userId = BaseContext.getContext();
        log.info("userId:{}退出群聊,gid:{}",userId,gid);
        groupService.exitGroup(userId,gid);
//        机器人播报
        chatUtil.GroupChatSend(gid);
        return Result.success(MessageConstant.OPERATESUCCESS);
    }
}
