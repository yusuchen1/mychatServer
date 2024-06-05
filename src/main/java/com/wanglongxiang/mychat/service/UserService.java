package com.wanglongxiang.mychat.service;

import com.wanglongxiang.mychat.pojo.dto.LoginUserDTO;
import com.wanglongxiang.mychat.pojo.dto.RegisterUserDTO;
import com.wanglongxiang.mychat.pojo.entity.User;

public interface UserService {
    public User login(LoginUserDTO loginUserDTO);
    public void register(RegisterUserDTO registerUserDTO);
}
