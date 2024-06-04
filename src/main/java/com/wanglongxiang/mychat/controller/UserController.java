package com.wanglongxiang.mychat.controller;

import com.wanglongxiang.mychat.common.Result;
import com.wanglongxiang.mychat.pojo.dto.LoginUserDTO;
import com.wanglongxiang.mychat.pojo.dto.RegisterUserDTO;
import com.wanglongxiang.mychat.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/login")
    public Result login(LoginUserDTO loginUserDTO){
        log.info("用户正在登录:{}",loginUserDTO);
        userService.login(loginUserDTO);
        return Result.success("登录成功！");
    }

    @PostMapping("/register")
    public Result register(@RequestBody RegisterUserDTO registerUserDTO){
        log.info("现在开始注册用户：{}",registerUserDTO);
        userService.register(registerUserDTO);
        return Result.success("注册成功!");
    }
}
