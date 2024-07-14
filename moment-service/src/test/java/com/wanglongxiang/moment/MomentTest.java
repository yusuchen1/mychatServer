package com.wanglongxiang.moment;

import com.wanglongxiang.moment.pojo.entity.Moments;
import com.wanglongxiang.moment.service.MomentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MomentTest {
    @Autowired
    MomentService momentService;
    @Test
    public void addMomentTest(){
        Moments moments = new Moments();
        moments.setUserId(2L);
        moments.setContent("河山大好，出去走走，碧海蓝天吹吹风");
        momentService.saveMoments(moments);
    }

    @Test
    public void reverseLikeTest(){
        momentService.reverseLike(2L,2L);
    }
}
