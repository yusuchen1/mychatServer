package com.wanglongxiang.mychat;

import com.wanglongxiang.mychat.pojo.entity.Crony;
import com.wanglongxiang.mychat.pojo.vo.AgreeCronyVO;
import com.wanglongxiang.mychat.service.CronyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CronyTest {
    @Autowired
    CronyService cronyService;
    @Test
    public void CronyAddTest(){
        AgreeCronyVO agreeCronyVO = new AgreeCronyVO(1L, 3L, "小苏");
        cronyService.agreeCrony(agreeCronyVO);
    }
}
