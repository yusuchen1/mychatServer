package com.wanglongxiang.mychat.controller;

import com.wanglongxiang.mychat.common.Code;
import com.wanglongxiang.mychat.common.Result;
import com.wanglongxiang.mychat.context.BaseContext;
import com.wanglongxiang.mychat.pojo.dto.ChatDTO;
import com.wanglongxiang.mychat.pojo.entity.Chat;
import com.wanglongxiang.mychat.pojo.vo.ChatVO;
import com.wanglongxiang.mychat.pojo.vo.LChatVO;
import com.wanglongxiang.mychat.service.ChatService;
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

    @PostMapping("/save")
    @ApiOperation("消息插入接口")
    public Result saveChat(@RequestBody ChatDTO chatDTO) throws IOException {
        log.info("正在插入消息:{}",chatDTO);
        Long userId = BaseContext.getContext();
        Chat chat = new Chat();
        BeanUtils.copyProperties(chatDTO,chat);
        chat.setSendUid(userId);
        chatService.save(chat);

//        给发送消息者推送新消息
        List<ChatVO> chatVOS1 = chatService.selectChat(chat.getSendUid(), chat.getReceiveUid());
        LChatVO lChatVO1 = new LChatVO(chat.getReceiveUid(),null, chatVOS1);
        echoChannel.sendClientByUids(lChatVO1,userId);

//        给接受消息者推送新消息
        List<ChatVO> chatVOS2 = chatService.selectChat(chat.getReceiveUid(), chat.getSendUid());
        LChatVO lChatVO2 = new LChatVO(userId,null, chatVOS2);
        echoChannel.sendClientByUids(lChatVO2,chat.getReceiveUid());
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
//        List<ChatVO> chatVOS = chatService.selectChat(userId, rid);
//        return new Result<>("查询成功！",chatVOS, Code.SUCESS);
        return Result.success();
    }
}
