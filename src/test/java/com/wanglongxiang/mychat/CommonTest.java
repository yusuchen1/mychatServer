package com.wanglongxiang.mychat;

import com.wanglongxiang.mychat.common.Result;
import com.wanglongxiang.mychat.controller.CommonController;
import com.wanglongxiang.mychat.pojo.other.CronyGroupList;
import com.wanglongxiang.mychat.pojo.other.GroupListItem;
import com.wanglongxiang.mychat.pojo.vo.CronyGroupAGroupVO;
import com.wanglongxiang.mychat.service.CommonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CommonTest {
//    @Autowired
//    CommonController commonController;
    @Autowired
    CommonService commonService;

    @Test
    public void getLeftMenu(){
//        Result<CronyGroupAGroupVO> leftMenu = commonController.getLeftMenu();
        Long userId = 2L;
        List<CronyGroupList> cronyGroupListByUid = commonService.getCronyGroupListByUid(userId);
        List<GroupListItem> groupListItemByUid = commonService.getGroupListItemByUid(userId);
        CronyGroupAGroupVO leftMenu = new CronyGroupAGroupVO(groupListItemByUid,cronyGroupListByUid);
        System.out.println(leftMenu);
    }
}
