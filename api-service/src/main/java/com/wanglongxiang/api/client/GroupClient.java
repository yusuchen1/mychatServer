package com.wanglongxiang.api.client;

import com.wanglongxiang.api.other.GroupListItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.websocket.server.PathParam;
import java.util.List;

@FeignClient("group-service")
public interface GroupClient {
    @GetMapping("/group/getUidsByGid")
    List<Long> getUidsByGid(@RequestParam("gid") Long gid);

    @GetMapping("/group/userIsInGroup")
    public boolean userIsInGroup(@RequestParam("userId")Long userId, @RequestParam("gid")Long gid);

    @GetMapping("/group/groupIsExists")
    public boolean groupIsExists(@RequestParam("gid")Long gid);

    @GetMapping("/group/getGidsByUid")
    public List<Long> getGidsByUid(@RequestParam("uid") Long uid);

    @GetMapping("/group/getGroupListItemsByUid")
    public List<GroupListItem> getGroupListItemsByUid(@RequestParam("uid") Long uid);
}
