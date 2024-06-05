package com.wanglongxiang.mychat.controller;

import com.wanglongxiang.mychat.common.Code;
import com.wanglongxiang.mychat.common.Result;
import com.wanglongxiang.mychat.common.constant.UserConstant;
import com.wanglongxiang.mychat.pojo.dto.LoginUserDTO;
import com.wanglongxiang.mychat.pojo.dto.RegisterUserDTO;
import com.wanglongxiang.mychat.pojo.entity.User;
import com.wanglongxiang.mychat.properties.JwtProperties;
import com.wanglongxiang.mychat.service.UserService;
import com.wanglongxiang.mychat.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    JwtProperties jwtProperties;

    @GetMapping("/login")
    public Result login(LoginUserDTO loginUserDTO){
        log.info("用户正在登录:{}",loginUserDTO);
        User u = userService.login(loginUserDTO);
        Map<String,Object> loginfo = new HashMap<>();
        loginfo.put(UserConstant.UID,u.getId());
        String jwt = JwtUtils.createJwt(jwtProperties.getSecretKey(), jwtProperties.getTtl(), loginfo);
        return new Result("登录成功！",jwt, Code.SUCESS);
    }

    @PostMapping("/register")
    public Result register(@RequestBody RegisterUserDTO registerUserDTO){
        log.info("现在开始注册用户：{}",registerUserDTO);
        userService.register(registerUserDTO);
        return Result.success("注册成功!");
    }
}
