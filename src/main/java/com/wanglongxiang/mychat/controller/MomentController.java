package com.wanglongxiang.mychat.controller;

import com.wanglongxiang.mychat.common.Code;
import com.wanglongxiang.mychat.common.Result;
import com.wanglongxiang.mychat.common.constant.MessageConstant;
import com.wanglongxiang.mychat.context.BaseContext;
import com.wanglongxiang.mychat.pojo.dto.dto.EditMomentDTO;
import com.wanglongxiang.mychat.pojo.dto.dto.MomentCommontDTO;
import com.wanglongxiang.mychat.pojo.entity.Moments;
import com.wanglongxiang.mychat.pojo.vo.CommontNicknameVO;
import com.wanglongxiang.mychat.pojo.vo.MomentsVO;
import com.wanglongxiang.mychat.service.MomentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Api(tags = "朋友圈相关接口")
@RestController
@RequestMapping("/moment")
public class MomentController {

    @Autowired
    MomentService momentService;

    @ApiOperation("新增或编辑朋友圈")
    @PostMapping("/addOrEditMoment")
    public Result addOrEditMoment(@RequestBody EditMomentDTO addMomentDTO){
        Long userId = BaseContext.getContext();
        if(addMomentDTO.getId() == null){
            log.info("正在新增朋友圈,userId:{},AddMomentDTO:{}",userId,addMomentDTO);
            Moments moments = new Moments();
            BeanUtils.copyProperties(addMomentDTO,moments);
            moments.setUserId(userId);
            momentService.saveMoments(moments);
        }else {
            log.info("正在编辑朋友圈,userId:{},AddMomentDTO:{}",userId,addMomentDTO);
            momentService.editMoments(addMomentDTO);
        }
        return Result.success(MessageConstant.SAVESUCCESS);
    }

    @ApiOperation("删除朋友圈")
    @DeleteMapping("/deleteMoment")
    public Result deleteMoment(@Param("id") Long id){
        Long userId = BaseContext.getContext();
        log.info("正在删除朋友圈,userId:{},momentId:{}",userId,id);
        momentService.deleteMomentById(id);
        return Result.success(MessageConstant.DELETESUCCESS);
    }

    @ApiOperation("获取朋友圈动态")
    @GetMapping
    public Result<List<MomentsVO>> getMoments(){
        Long userId = BaseContext.getContext();
        log.info("正在获取朋友圈信息:userId:{}",userId);
        List<MomentsVO> MomentsVOS = momentService.getMomentsVOS(userId);
        return new Result<>(MomentsVOS);
    }

    @ApiOperation("点赞或取消点赞朋友圈")
    @PostMapping("/like")
    public Result<String> like(@RequestParam("momentId") Long momentId){
        Long userId = BaseContext.getContext();
        log.info("正在点赞或取消点赞朋友圈,userId:{},momentId:{}",userId,momentId);
        String nickname = momentService.reverseLike(momentId, userId);
        return new Result<>(MessageConstant.OPERATESUCCESS,nickname, Code.SUCESS);
    }

    @ApiOperation("评论朋友圈")
    @PostMapping("/addComment")
    public Result<CommontNicknameVO> addComment(MomentCommontDTO momentCommontDTO){
        Long userId = BaseContext.getContext();
        log.info("正在对朋友圈插入评论:userId:{},momentCommontDTO:{}",userId,momentCommontDTO);
        CommontNicknameVO commontNicknameVO = momentService.saveCommont(userId, momentCommontDTO);
        return new Result<>(MessageConstant.OPERATESUCCESS,commontNicknameVO,Code.SUCESS);
    }

    @ApiOperation("删除评论")
    @DeleteMapping("/deleteComment")
    public Result<Long> deleteComment(@Param("momentsDetailsId") Long momentsDetailsId){
        Long userId = BaseContext.getContext();
        log.info("正在删除评论:userId:{},momentsDetailsId:{}",userId,momentsDetailsId);
        momentService.deleteCommont(userId, momentsDetailsId);
        return new Result<>(MessageConstant.DELETESUCCESS);
    }

    @ApiOperation("更新用户昵称")
    @PutMapping("/updateNickname")
    public void updateNickname(@RequestBody Map<Long,String> map){
        log.info("正在更新用户昵称:{}",map);
        momentService.updateNickname(map);
    }
}
