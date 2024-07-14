package com.wanglongxiang.api.client;

import com.wanglongxiang.api.other.CronyGroupList;
import com.wanglongxiang.api.vo.CronyDesAndCGidVO;
import com.wanglongxiang.api.vo.CronyGroupVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient(value="crony-service")
public interface CronyClient {
    @GetMapping("/crony/isCrony")
    boolean isCrony(@RequestParam("userId1") Long userId1,@RequestParam("userId2") Long userId2);

    @GetMapping("/crony/getCronyGroupVOS")
    List<CronyGroupVO> getCronyGroupVOS();

    @GetMapping("/crony/getCronyDesAndCGidVO")
    CronyDesAndCGidVO getCronyDesAndCGidVO(@RequestParam("userId") Long userId, @RequestParam("cronyId") Long cronyId);

    @PostMapping("/crony/addCronyGroupByUid")
    boolean addCronyGroupByUid(@RequestParam("userId") Long userId, @RequestParam("groupName") String groupName);

    @GetMapping("/crony/getCronyGroupListsByUid")
    List<CronyGroupList> getCronyGroupListsByUid(@RequestParam("uid") Long uid);

    @GetMapping("/crony/getCronyDesAndCGidVOs")
    List<CronyDesAndCGidVO> getCronyDesAndCGidVOs(@RequestParam("userId") Long userId,@RequestParam("cronyIds") List<Long> cronyIds);

    @GetMapping("/crony/getCronyIds")
    List<Long> getCronyIds(@RequestParam("userId") Long userId);
}
