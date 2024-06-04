package com.wanglongxiang.mychat.service;

import com.wanglongxiang.mychat.pojo.dto.LoginUserDTO;
import com.wanglongxiang.mychat.pojo.dto.RegisterUserDTO;

public interface UserService {
    public void login(LoginUserDTO loginUserDTO);
    public void register(RegisterUserDTO registerUserDTO);
}
