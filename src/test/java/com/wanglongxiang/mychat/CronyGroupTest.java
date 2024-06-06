package com.wanglongxiang.mychat;

import com.wanglongxiang.mychat.service.CronyGroupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CronyGroupTest {
    @Autowired
    CronyGroupService cronyGroupService;
    @Test
    public void saveTest(){
        cronyGroupService.save(2L,"好友");
    }
}
