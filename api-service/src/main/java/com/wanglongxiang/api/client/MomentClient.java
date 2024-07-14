package com.wanglongxiang.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient("moment-service")
public interface MomentClient {
    @PutMapping("/moment/updateNickname")
    void updateNickname(@RequestBody Map<Long,String> map);
}
