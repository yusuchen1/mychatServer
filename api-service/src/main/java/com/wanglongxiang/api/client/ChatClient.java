package com.wanglongxiang.api.client;

import com.wanglongxiang.api.other.CronyListItem;
import com.wanglongxiang.api.vo.ChatVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("chat-service")
public interface ChatClient {
    @GetMapping("/chat/groupChatSend")
    boolean groupChatSend(@RequestParam("gid")Long gid);

    @GetMapping("/chat/getChatVOSByGid")
    List<ChatVO> getChatVOSByGid(@RequestParam("gid") Long gid);

    @DeleteMapping("/chat/delGroupChatByGid")
    boolean delGroupChatByGid(Long gid);

    @PostMapping("/chat/customGroupSave")
    boolean customSave(
            @RequestParam("content") String content,
            @RequestParam("sendUid") Long sendUid,
            @RequestParam("gid") Long gid
    );

    @GetMapping("/chat/setChatsForCLI")
    CronyListItem setChatsForCLI(@RequestParam("cli") CronyListItem cli,
                                        @RequestParam("objId") Long objId,
                                        @RequestParam("aksId") Long askId);

    @GetMapping("/chat/getChatVOS")
    List<ChatVO> getChatVOS(@RequestParam("sid") Long sid,@RequestParam("rid") Long rid);

    @GetMapping("/chat/getChatVOSS")
    List<List<ChatVO>> getChatVOSS(@RequestParam("sid") Long sid,@RequestParam("rids") List<Long> rids);
}
