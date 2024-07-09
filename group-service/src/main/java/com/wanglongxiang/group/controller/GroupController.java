package com.wanglongxiang.group.controller;

import com.wanglongxiang.api.client.ChatClient;
import com.wanglongxiang.api.dto.GroupInfoDTO;
import com.wanglongxiang.api.dto.SearchDTO;
import com.wanglongxiang.api.other.GroupListItem;
import com.wanglongxiang.api.vo.GroupMVO;
import com.wanglongxiang.api.vo.SearchGroupVO;
import com.wanglongxiang.group.pojo.entity.Group;
import com.wanglongxiang.group.service.GroupService;
import com.wanglongxiang.mychat.common.Code;
import com.wanglongxiang.mychat.common.Result;
import com.wanglongxiang.mychat.common.ResultPage;
import com.wanglongxiang.mychat.common.constant.MessageConstant;
import com.wanglongxiang.mychat.context.BaseContext;
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
    ChatClient chatClient;

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
        chatClient.groupChatSend(gid);
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
//        推送机器人播报
        chatClient.groupChatSend(gid);
        return Result.success(MessageConstant.OPERATESUCCESS);
    }

    @PutMapping("/updateGroup")
    @ApiOperation("更新群聊")
    public Result updateGroup(@RequestBody Group group){
        Long userId = BaseContext.getContext();
        log.info("userId:{}正在更新群组:{}",userId,group);
        groupService.updateGroup(group);
        return Result.success(MessageConstant.UPDATESUCCESS);
    }

    @DeleteMapping("/dissGroup")
    @ApiOperation("解散群聊")
    public Result dissGroup(@PathParam("gid") Long gid){
        Long userId = BaseContext.getContext();
        log.info("userId:{}正在解散群聊,gid:{}",userId,gid);
        groupService.dissGroup(userId,gid);
        return Result.success(MessageConstant.OPERATESUCCESS);
    }

    @ApiOperation("根据群聊id获取到群聊中所有用户的id")
    @GetMapping("/getUidsByGid")
    public List<Long> getUidsByGid(@PathParam("gid") Long gid){
        log.info("根据群聊id获取群聊所有成员");
        return groupService.getUidsByGid(gid);
    }

    @ApiOperation("判断用户是否在群成员表中")
    @GetMapping("/userIsInGroup")
    public boolean userIsInGroup(@RequestParam("userId")Long userId,@RequestParam("gid")Long gid){
        return groupService.userIsInGroup(userId,gid);
    }

    @ApiOperation("判断群聊是否存在")
    @GetMapping("/groupIsExists")
    public boolean groupIsExists(@RequestParam("gid")Long gid){
        return groupService.groupIsExists(gid);
    }

    @ApiOperation("根据用户id获取到所有的群聊id")
    @GetMapping("/getGidsByUid")
    public List<Long> getGidsByUid(@RequestParam("uid") Long uid){
        return groupService.getGidsByUid(uid);
    }

    @ApiOperation("根据用户id获取到所有的群聊记录vo")
    @GetMapping("/getGroupListItemsByUid")
    public List<GroupListItem> getGroupListItemsByUid(@RequestParam("uid") Long uid){
        return groupService.getGroupListItemsByUid(uid);
    }
}
