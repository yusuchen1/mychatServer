package com.wanglongxiang.mychat.service;

import com.wanglongxiang.mychat.common.ResultPage;
import com.wanglongxiang.mychat.pojo.dto.LoginUserDTO;
import com.wanglongxiang.mychat.pojo.dto.RegisterUserDTO;
import com.wanglongxiang.mychat.pojo.dto.SearchUserDTO;
import com.wanglongxiang.mychat.pojo.entity.User;
import com.wanglongxiang.mychat.pojo.vo.SearchUserVO;

import java.util.List;

public interface UserService {
    public User login(LoginUserDTO loginUserDTO);
    public User register(RegisterUserDTO registerUserDTO);
    public ResultPage searchUser(SearchUserDTO searchUserDTO, Long userId);
}
