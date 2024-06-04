package com.wanglongxiang.mychat;

import com.wanglongxiang.mychat.common.constant.UserConstant;
import com.wanglongxiang.mychat.mapper.UserMapper;
import com.wanglongxiang.mychat.pojo.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserTest {
    @Autowired
    UserMapper userMapper;
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
}
