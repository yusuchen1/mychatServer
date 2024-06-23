package com.wanglongxiang.mychat;

import com.wanglongxiang.mychat.common.ResultPage;
import com.wanglongxiang.mychat.common.constant.GroupConstant;
import com.wanglongxiang.mychat.common.constant.UserConstant;
import com.wanglongxiang.mychat.mapper.UserMapper;
import com.wanglongxiang.mychat.pojo.dto.SearchDTO;
import com.wanglongxiang.mychat.pojo.entity.User;
import com.wanglongxiang.mychat.pojo.vo.UserInfoVO;
import com.wanglongxiang.mychat.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class UserTest {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserService userService;

    @Test
    public void getByUsernameTest(){
    }

    @Test
    public void saveTest(){
        User user = new User();
        user.setAvatar(UserConstant.defaultAvatar);
        user.setUsername("suchen");
        user.setPassword("123456");
        user.setNickname("苏辰");
        user.setSex("男");
        user.setPhone("12346578912");
        userMapper.save(user);
    }

    @Test
    public void searchByUsernameOrNicknameTest(){
        List<User> us = userMapper.searchByUsernameOrNickname("n",1,1,2L, GroupConstant.ROBOTID);
        System.out.println(us);
    }

    @Test
    public void searchUserTest(){
        SearchDTO searchDTO = new SearchDTO("n", 1, 5);
        ResultPage resultPage = userService.searchUser(searchDTO, 2L);
        System.out.println(resultPage);
    }

    @Test
    public void getUserInfo(){
        UserInfoVO userInfo = userService.getOtherInfo(4L, 5L);
        System.out.println(userInfo);
    }
}
