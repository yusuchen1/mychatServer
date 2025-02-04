package com.wanglongxiang.mychat.controller;

import com.wanglongxiang.mychat.common.constant.CronyConstant;
import com.wanglongxiang.mychat.common.constant.GroupConstant;
import com.wanglongxiang.mychat.context.BaseContext;
import com.wanglongxiang.mychat.common.Code;
import com.wanglongxiang.mychat.common.Result;
import com.wanglongxiang.mychat.common.ResultPage;
import com.wanglongxiang.mychat.common.constant.MessageConstant;
import com.wanglongxiang.mychat.common.constant.UserConstant;
import com.wanglongxiang.mychat.pojo.dto.LoginUserDTO;
import com.wanglongxiang.mychat.pojo.dto.PasswordDTO;
import com.wanglongxiang.mychat.pojo.dto.RegisterUserDTO;
import com.wanglongxiang.mychat.pojo.dto.SearchDTO;
import com.wanglongxiang.mychat.pojo.entity.User;
import com.wanglongxiang.mychat.pojo.vo.SearchUserVO;
import com.wanglongxiang.mychat.pojo.vo.UserInfoVO;
import com.wanglongxiang.mychat.properties.JwtProperties;
import com.wanglongxiang.mychat.service.CronyGroupService;
import com.wanglongxiang.mychat.service.UserService;
import com.wanglongxiang.mychat.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
@Api(tags = {"用户相关接口"})
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    JwtProperties jwtProperties;
    @Autowired
    CronyGroupService cronyGroupService;

    @GetMapping("/login")
    @ApiOperation("用户登录")
    public Result login(LoginUserDTO loginUserDTO){
        log.info("用户正在登录:{}",loginUserDTO);
        User u = userService.login(loginUserDTO);
        Map<String,Object> loginfo = new HashMap<>();
        loginfo.put(UserConstant.UID,u.getId());
        String jwt = JwtUtils.createJwt(jwtProperties.getSecretKey(), jwtProperties.getTtl(), loginfo);
        return new Result("登录成功！",jwt, Code.SUCESS);
    }

    @PostMapping("/register")
    @ApiOperation("用户注册")
    public Result register(@RequestBody RegisterUserDTO registerUserDTO){
        log.info("现在开始注册用户：{}",registerUserDTO);
        User register = userService.register(registerUserDTO);
        cronyGroupService.save(register.getId(), CronyConstant.DEFAULTCRONYGROUP);
        return Result.success("注册成功!");
    }

    @GetMapping("/searchUser")
    @ApiOperation("查询用户")
    public Result<ResultPage<SearchUserVO>> searchUser(SearchDTO searchDTO){
        Long userId = BaseContext.getContext();
        log.info("现在正在查询用户:{},userId:{}", searchDTO,userId);
        ResultPage<SearchUserVO> resultPage = userService.searchUser(searchDTO, userId);
        return new Result<>(resultPage);
    }

    @GetMapping("/getOtherInfo")
    @ApiOperation("获取其他用户信息")
    public Result<UserInfoVO> getUserInfo(Long uid){
        Long userId = BaseContext.getContext();
        log.info("获取其他用户信息:{}",uid);
        UserInfoVO userInfo = userService.getOtherInfo(userId, uid);
        return new Result<>(userInfo);
    }

    @GetMapping("/getAvatar")
    @ApiOperation("获取头像")
    public Result<String> getAvatar(){
        Long userId = BaseContext.getContext();
        log.info("获取用户头像:{}",userId);
        String avatar = userService.getAvatar(userId);
        return new Result<>(MessageConstant.OPERATESUCCESS,avatar,Code.SUCESS);
    }

    @PostMapping("/uploadAvatar")
    @ApiOperation(("上传头像"))
    public Result<String> upload(MultipartFile file) throws IOException {
        Long userId = BaseContext.getContext();
        log.info("userId:{}现在开始上传头像:{}",userId,file);
        String avatar = userService.uploadAvatar(userId,file);
        return new Result<>(MessageConstant.UPLOADSUCCESS,avatar,Code.SUCESS);
    }

    @GetMapping("/getUserInfo")
    @ApiOperation("获取用户信息")
    public Result<User> getUserInfo(){
        Long userId = BaseContext.getContext();
        log.info("获取用户信息,userId:{}",userId);
        User u = userService.getUserInfo(userId);
        return new Result<>(u);
    }

    @PutMapping("/updateUserInfo")
    @ApiOperation("更新用户信息")
    public Result updateUserInfo(@RequestBody User user){
        log.info("更新用户信息:{}",user);
        userService.updateUser(user);
        return Result.success(MessageConstant.OPERATESUCCESS);
    }

    @PutMapping("/updatePassword")
    @ApiOperation("修改用户密码")
    public Result updatePassword(@RequestBody PasswordDTO passwordDTO){
        Long userId = BaseContext.getContext();
        log.info("userId:{},正在修改密码:{}",userId,passwordDTO);
        userService.updatePassword(userId,passwordDTO);
        return Result.success(MessageConstant.CHANGESUCCESS);
    }
}
