package com.wanglongxiang.mychat;

import com.wanglongxiang.mychat.mapper.CronyAskMapper;
import com.wanglongxiang.mychat.pojo.entity.CronyAsk;
import com.wanglongxiang.mychat.service.CronyAskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CronyAskTest {
    @Autowired
    CronyAskService cronyAskService;
    @Autowired
    CronyAskMapper cronyAskMapper;
    @Test
    public void saveTest(){
        CronyAsk cronyAsk = new CronyAsk();
        cronyAsk.setAskId(2L);
        cronyAsk.setObjId(3L);
        cronyAsk.setAskCronyGroupId(1L);
        cronyAskService.save(cronyAsk);
    }

    @Test
    public void mpSelectByIdTest(){
        CronyAsk cronyAsk = cronyAskMapper.selectById(1L);
        System.out.println(cronyAsk);
    }
}
