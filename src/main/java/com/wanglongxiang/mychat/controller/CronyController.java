package com.wanglongxiang.mychat.controller;

import com.wanglongxiang.mychat.common.Code;
import com.wanglongxiang.mychat.common.Result;
import com.wanglongxiang.mychat.common.constant.MessageConstant;
import com.wanglongxiang.mychat.context.BaseContext;
import com.wanglongxiang.mychat.pojo.dto.CronyAddDTO;
import com.wanglongxiang.mychat.pojo.dto.UpdateCronyInfoDTO;
import com.wanglongxiang.mychat.pojo.entity.CronyAsk;
import com.wanglongxiang.mychat.pojo.entity.CronyGroup;
import com.wanglongxiang.mychat.pojo.other.CronyGroupList;
import com.wanglongxiang.mychat.pojo.vo.AgreeCronyVO;
import com.wanglongxiang.mychat.pojo.vo.CronyGroupVO;
import com.wanglongxiang.mychat.pojo.vo.SearchUserVO;
import com.wanglongxiang.mychat.pojo.vo.UserMessageVO;
import com.wanglongxiang.mychat.service.CronyAskService;
import com.wanglongxiang.mychat.service.CronyGroupService;
import com.wanglongxiang.mychat.service.CronyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@Api(tags = "好友相关接口")
@Slf4j
@RestController
@RequestMapping("/crony")
public class CronyController {

    @Autowired
    CronyService cronyService;

    @Autowired
    CronyAskService cronyAskService;

    @Autowired
    CronyGroupService cronyGroupService;

    @ApiOperation("发送添加好友申请")
    @PostMapping("cronyAdd")
    public Result addCrony(@RequestBody CronyAddDTO cronyAddDTO){
        Long userId = BaseContext.getContext();
        log.info("userId:{}添加好友:{}",userId,cronyAddDTO);
        if(cronyAddDTO.getCronyAskId().equals(userId)){
            return Result.error("请不要添加自己为好友");
        }
        CronyAsk cronyAsk = new CronyAsk();
        cronyAsk.setDescription(cronyAddDTO.getDescription());
        cronyAsk.setAskId(userId);
        cronyAsk.setAskCronyGroupId(cronyAddDTO.getCronyGroupId());
        cronyAsk.setObjId(cronyAddDTO.getCronyAskId());
        cronyAskService.save(cronyAsk);
        return Result.success(MessageConstant.OPERATESUCCESS);
    }

    @ApiOperation("新增好友组")
    @PostMapping("/addCronyGroup")
    public Result<List<CronyGroupVO>> addCronyGroup(@RequestParam String groupName){
        Long userId = BaseContext.getContext();
        log.info("添加好友分组:{},userId:{}",groupName,userId);
        cronyGroupService.save(userId,groupName);
        List<CronyGroupVO> groupVOS = cronyGroupService.getCronyGroupByUid(userId);
        return new Result<>(MessageConstant.OPERATESUCCESS,groupVOS, Code.SUCESS);
    }

    @ApiOperation("获取用户消息")
    @GetMapping("/getUserMessage")
    public Result<List<UserMessageVO>> getUserMessage(){
        Long userId = BaseContext.getContext();
        List<UserMessageVO> userMessageVOS = cronyAskService.getUserMessageVOS(userId);
        return new Result(MessageConstant.OPERATESUCCESS,userMessageVOS,Code.SUCESS);
    }

    @ApiOperation("同意好友申请")
    @PostMapping("/agreeCrony")
    public Result<CronyGroupList> agreeCrony(@RequestBody AgreeCronyVO agreeCronyVO){
        Long userId = BaseContext.getContext();
        log.info("同意好友申请,userId:{},AgreeCronyVO:{}",userId,agreeCronyVO);
        CronyGroupList cronyGroupList = cronyService.agreeCrony(agreeCronyVO);
        return new Result<>(MessageConstant.OPERATESUCCESS,cronyGroupList,Code.SUCESS);
    }

    @ApiOperation("拒绝好友申请")
    @DeleteMapping("/refuseCrony/{askId}")
    public Result refuseCrony(@PathVariable("askId") Long askId){
        Long userId = BaseContext.getContext();
        log.info("userId{},正在拒绝好友申请，cronyAskId:{}",userId,askId);
        cronyService.refuseCronyAsk(askId);
        return Result.success(MessageConstant.OPERATESUCCESS);
    }

    @ApiOperation("更新好友备注")
    @PutMapping("/updataCronyInfo")
    public Result updataCronyInfo(@RequestBody UpdateCronyInfoDTO updateCronyInfoDTO){
        Long userId = BaseContext.getContext();
        log.info("userId:{}正在更新好友信息:{}",userId,updateCronyInfoDTO);
        cronyService.updateCronyInfo(userId,updateCronyInfoDTO);
        return Result.success(MessageConstant.OPERATESUCCESS+MessageConstant.PLEASEREFRESH);
    }

    @DeleteMapping("/deleteCronyGroup")
    @ApiOperation("删除好友分组")
    public Result deleteCronyGroup(@RequestParam("groupId") Long groupId){
        Long userId = BaseContext.getContext();
        log.info("userId{},正在删除好友组:{}",userId,groupId);
        cronyGroupService.deleteGroup(userId,groupId);
        return Result.success(MessageConstant.DELETESUCCESS+"如有该组的好友申请未被同意，则请重新发送好友申请。");
    }

    @GetMapping("/getCronyGroup")
    @ApiOperation("获取用户的所有好友分组")
    public Result<List<CronyGroup>> getCronyGroup(){
        Long userId = BaseContext.getContext();
        log.info("用户正在获取所有好友组:userId:{}",userId);
        List<CronyGroup> cronyGroupList = cronyGroupService.getCronyGroup(userId);
        return new Result<>(cronyGroupList);
    }

    @DeleteMapping("/dropCrony")
    @ApiOperation("删除好友")
    public Result DropCrony(@PathParam("cronyId") Long cronyId){
        Long userId = BaseContext.getContext();
        log.info("userId:{}正在删除好友,好友id:{}",userId,cronyId);
        cronyService.dropCrony(userId,cronyId);
        return Result.success(MessageConstant.DELETESUCCESS);
    }

    @GetMapping("/groupMember")
    @ApiOperation("获取群聊成员")
    public Result<List<SearchUserVO>> getGroupMember(Long gid){
        Long userId = BaseContext.getContext();
        log.info("userId:{},获取群聊成员:{}",userId,gid);
        List<SearchUserVO> searchUserVOS = cronyService.getUserByGid(userId, gid,"");
        return new Result<>(searchUserVOS);
    }
}
