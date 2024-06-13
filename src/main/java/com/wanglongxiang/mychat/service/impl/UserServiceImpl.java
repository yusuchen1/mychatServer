package com.wanglongxiang.mychat.service.impl;

import com.wanglongxiang.mychat.exception.UserException.PasswordErrorException;
import com.wanglongxiang.mychat.exception.UserException.UserExistException;
import com.wanglongxiang.mychat.exception.UserException.UserNotFoundException;
import com.wanglongxiang.mychat.common.ResultPage;
import com.wanglongxiang.mychat.common.constant.MessageConstant;
import com.wanglongxiang.mychat.common.constant.UserConstant;
import com.wanglongxiang.mychat.mapper.CronyMapper;
import com.wanglongxiang.mychat.mapper.UserMapper;
import com.wanglongxiang.mychat.pojo.dto.LoginUserDTO;
import com.wanglongxiang.mychat.pojo.dto.RegisterUserDTO;
import com.wanglongxiang.mychat.pojo.dto.SearchUserDTO;
import com.wanglongxiang.mychat.pojo.entity.Crony;
import com.wanglongxiang.mychat.pojo.entity.User;
import com.wanglongxiang.mychat.pojo.vo.SearchUserVO;
import com.wanglongxiang.mychat.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    CronyMapper cronyMapper;

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

    @Override
    public ResultPage<SearchUserVO> searchUser(SearchUserDTO searchUserDTO, Long userId) {
        ResultPage resultPage = new ResultPage();
//        获取查询条件
        String key = searchUserDTO.getKey();
        Integer size = searchUserDTO.getSize();
        Integer page = searchUserDTO.getPage();
        List<User> users = userMapper.searchByUsernameOrNickname(key, page, size,userId);
        Integer count = userMapper.searchCountByUsernameOrNickname(key, page, size,userId);
//        设置resultPage参数
        resultPage.setPageNum(page/size + 1);
        resultPage.setPageSize(size);
        List<SearchUserVO> searchUserVOS = new ArrayList<>();
        for (User user : users) {
            SearchUserVO searchUserVO = new SearchUserVO();
            Crony crony = cronyMapper.selectByUserIdAndCronyId(userId, user.getId());
//            给searchUserVO赋值
            BeanUtils.copyProperties(user,searchUserVO);
            searchUserVO.setCrony(!(crony == null));
            searchUserVOS.add(searchUserVO);
        }
        resultPage.setData(searchUserVOS);
        resultPage.setTotal(count);
        return resultPage;
    }
}
