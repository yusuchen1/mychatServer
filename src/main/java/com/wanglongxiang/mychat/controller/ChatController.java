package com.wanglongxiang.mychat.controller;

import com.wanglongxiang.mychat.common.Code;
import com.wanglongxiang.mychat.common.Result;
import com.wanglongxiang.mychat.common.constant.MessageConstant;
import com.wanglongxiang.mychat.context.BaseContext;
import com.wanglongxiang.mychat.pojo.dto.ChatDTO;
import com.wanglongxiang.mychat.pojo.entity.Chat;
import com.wanglongxiang.mychat.pojo.vo.ChatVO;
import com.wanglongxiang.mychat.pojo.vo.LChatVO;
import com.wanglongxiang.mychat.service.ChatService;
import com.wanglongxiang.mychat.service.GroupService;
import com.wanglongxiang.mychat.utils.ChatUtil;
import com.wanglongxiang.mychat.webSocket.EchoChannel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Api(tags = "聊天相关接口")
@Slf4j
@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    ChatService chatService;
    @Autowired
    EchoChannel echoChannel;
    @Autowired
    GroupService groupService;
    @Autowired
    ChatUtil chatUtil;

    @PostMapping("/save")
    @ApiOperation("消息插入接口")
    public Result saveChat(@RequestBody ChatDTO chatDTO) throws IOException {
        log.info("正在插入消息:{}",chatDTO);
        Long userId = BaseContext.getContext();
        Chat chat = new Chat();
        BeanUtils.copyProperties(chatDTO,chat);
        chat.setSendUid(userId);
        chatService.save(chat);

//        好友之间互发消息,webSocket给双方发送更新后的消息
        if(chatDTO.getReceiveUid() != null){
            Long uid1 = chat.getSendUid();
            Long uid2 = chat.getReceiveUid();
            chatUtil.CronyChatSend(uid1, uid2);
        }

//        群组发消息
        else {
            chatUtil.GroupChatSend(chatDTO.getGroupId());
        }
        return  Result.success("发送成功!");
    }




    @GetMapping("/{rid}")
    @ApiOperation("查询聊天记录接口")
    public Result<List<ChatVO>> getChat(@PathVariable Long rid){
        Long userId = BaseContext.getContext();
        log.info("正在查询聊天记录:uid:{},rid:{}",userId,rid);
        List<ChatVO> chatVOS = chatService.selectChat(userId, rid);
        return new Result<>("查询成功！",chatVOS, Code.SUCESS);
    }

    @GetMapping("/g/{gid}")
    @ApiOperation("查询群聊天记录接口")
    public Result<List<ChatVO>> getGroupChat(@PathVariable Long gid){
        Long userId = BaseContext.getContext();
        log.info("正在查询聊天记录:uid:{},rid:{}",userId,gid);
        return Result.success();
    }

    @ApiOperation("撤回消息")
    @DeleteMapping("/delChat")
    public Result delChat(@RequestParam("chatId") Long chatId){
        Long userId = BaseContext.getContext();
        log.info("正在撤回消息,userId:{},chatId:{}",userId,chatId);
        chatService.deleteChat(chatId);
        return Result.success(MessageConstant.REVOKESUCCESS);
    }
}
