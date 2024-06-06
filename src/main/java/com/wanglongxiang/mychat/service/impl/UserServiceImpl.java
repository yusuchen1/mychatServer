package com.wanglongxiang.mychat.service.impl;

import com.wanglongxiang.mychat.Exception.UserException.PasswordErrorException;
import com.wanglongxiang.mychat.Exception.UserException.UserExistException;
import com.wanglongxiang.mychat.Exception.UserException.UserNotFoundException;
import com.wanglongxiang.mychat.common.constant.MessageConstant;
import com.wanglongxiang.mychat.common.constant.UserConstant;
import com.wanglongxiang.mychat.mapper.UserMapper;
import com.wanglongxiang.mychat.pojo.dto.LoginUserDTO;
import com.wanglongxiang.mychat.pojo.dto.RegisterUserDTO;
import com.wanglongxiang.mychat.pojo.entity.User;
import com.wanglongxiang.mychat.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public User login(LoginUserDTO loginUserDTO) {
        String username = loginUserDTO.getUsername();
        String password = loginUserDTO.getPassword();
        User u = userMapper.getByUsername(username);
        if(u == null){
            throw new UserNotFoundException(MessageConstant.USERNOTFOUND);
        }else if(!u.getPassword().equals(password)){
            throw new PasswordErrorException(MessageConstant.PASSWORDERROR);
        }
        return u;
    }

    @Override
    public User register(RegisterUserDTO registerUserDTO) {
        User user = new User();
        BeanUtils.copyProperties(registerUserDTO,user);
        user.setAvatar(UserConstant.defaultAvatar);
        User u = userMapper.getByUsername(user.getUsername());
        if(u != null){
            throw new UserExistException(MessageConstant.USEREXIST);
        }
        userMapper.save(user);
        return user;
    }
}
