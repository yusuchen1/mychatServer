package com.wanglongxiang.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(value = "common-service")
public interface CommonClient {
    @GetMapping("/common/online")
    void onLine(@RequestParam("userId") Long userId);

    @GetMapping("/common/offline")
    void offLine(@RequestParam("userId") Long userId);
}
