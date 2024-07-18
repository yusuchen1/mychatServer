package com.wanglongxiang.chat.controller;

import com.wanglongxiang.api.dto.ChatDTO;
import com.wanglongxiang.api.other.CronyListItem;
import com.wanglongxiang.api.vo.ChatVO;
import com.wanglongxiang.chat.pojo.entity.Chat;
import com.wanglongxiang.chat.service.ChatService;
import com.wanglongxiang.chat.utils.ChatUtil;
import com.wanglongxiang.chat.webSocket.EchoChannel;
import com.wanglongxiang.mychat.common.Code;
import com.wanglongxiang.mychat.common.Result;
import com.wanglongxiang.mychat.common.constant.MessageConstant;
import com.wanglongxiang.mychat.context.BaseContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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

    @ApiOperation("自定义群聊消息插入接口")
    @PostMapping("/customGroupSave")
    public boolean customSave(
            @RequestParam("content") String content,
            @RequestParam("sendUid") Long sendUid,
            @RequestParam("gid") Long gid
            ){
        Chat chat = new Chat();
        chat.setSendUid(sendUid);
        chat.setContent(content);
        chat.setGroupId(gid);
        log.info("正在插入自定义群聊消息:{}",chat);
        chatService.save(chat);
        return true;
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

    @ApiOperation("对群聊推送消息")
    @GetMapping("/groupChatSend")
    public boolean groupChatSend(@RequestParam("gid")Long gid){
        log.info("对群聊推送消息,gid:{}",gid);
        try {
            chatUtil.GroupChatSend(gid);
        } catch (IOException e) {
            throw new RuntimeException();
        }
        return true;
    }

    @ApiOperation("根据群聊信息获取聊天记录")
    @GetMapping("/getChatVOSByGid")
    public List<ChatVO> getChatVOSByGid(@RequestParam("gid") Long gid){
        Long userId = BaseContext.getContext();
        log.info("用户:{}根据群聊id获取聊天记录,gid:{}",userId,gid);
        return chatService.getChatVOSByGid(userId,gid);
    }

    @ApiOperation("根据群聊id删除群聊的所有聊天记录")
    @DeleteMapping("/delGroupChatByGid")
    public boolean delGroupChatByGid(Long gid){
        chatService.delGroupChatByGid(gid);
        return true;
    }

    @ApiOperation("给CronyListItem组装chats")
    @GetMapping("/setChatsForCLI")
    public CronyListItem setChatsForCLI(@RequestParam("cli") CronyListItem cli,
                                        @RequestParam("objId") Long objId,
                                        @RequestParam("aksId") Long askId){
        return chatService.setChatsForCLI(cli,objId,askId);
    }

    @ApiOperation("获取用户之间的聊天消息")
    @GetMapping("/getChatVOS")
    public List<ChatVO> getChatVOS(@RequestParam("sid") Long sid,@RequestParam("rid") Long rid){
        return chatService.getChatVOS(sid,rid);
    }

    @ApiOperation("获取指定用户与一个用户集合的聊天消息")
    @GetMapping("/getChatVOSS")
    public List<List<ChatVO>> getChatVOSS(@RequestParam("sid") Long sid,@RequestParam("rids") List<Long> rids){
        log.info("左侧列表:rids={}",rids);
        return chatService.getChatVOSS(sid,rids);
    }

    @ApiOperation("撤回消息")
    @DeleteMapping("/delChat")
    public Result delChat(@RequestParam("chatId") Long chatId){
        Long userId = BaseContext.getContext();
        log.info("正在撤回消息,userId:{},chatId:{}",userId,chatId);
        Long resId = chatService.deleteChat(chatId);

        try {
            chatUtil.CronyChatSend(userId, resId);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Result.success(MessageConstant.REVOKESUCCESS);
    }
}
