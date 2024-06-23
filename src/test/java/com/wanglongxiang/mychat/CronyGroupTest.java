package com.wanglongxiang.mychat;

import com.wanglongxiang.mychat.service.CronyGroupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class CronyGroupTest {
    @Autowired
    CronyGroupService cronyGroupService;
    @Test
    public void saveTest(){
//        cronyGroupService.save(2L,"好友");
        cronyGroupService.save(2L,"家人");
    }

    @Test
    public void getAllCronyGroupByUidTest(){
        System.out.println(cronyGroupService.getCronyGroupByUid(2L));
    }

    @Test void deleteGroupTest(){
        cronyGroupService.deleteGroup(9L,17L);
    }
}
