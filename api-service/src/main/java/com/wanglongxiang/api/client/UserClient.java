package com.wanglongxiang.api.client;

import com.wanglongxiang.api.vo.SearchUserVO;
import com.wanglongxiang.api.vo.UserChatInfoVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("user-service")
public interface UserClient {
    @GetMapping("/user/getUserChatInfo")
    UserChatInfoVO getUserChatInfo(@RequestParam("uid") Long uid);

    @GetMapping("/user/getSearchUserVOSByUids")
    List<SearchUserVO> getSearchUserVOSByUids(List<Long> uids);

    @GetMapping("/user/getUserChatInfos")
    List<UserChatInfoVO> getUserChatInfos(@RequestParam("uids") List<Long> uids);
}
