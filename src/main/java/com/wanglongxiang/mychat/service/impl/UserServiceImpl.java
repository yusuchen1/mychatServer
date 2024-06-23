package com.wanglongxiang.mychat.service.impl;

import com.wanglongxiang.mychat.common.constant.GroupConstant;
import com.wanglongxiang.mychat.exception.NoPermissionExecption;
import com.wanglongxiang.mychat.exception.UserException.PasswordErrorException;
import com.wanglongxiang.mychat.exception.UserException.UserExistException;
import com.wanglongxiang.mychat.exception.UserException.UserNotFoundException;
import com.wanglongxiang.mychat.common.ResultPage;
import com.wanglongxiang.mychat.common.constant.MessageConstant;
import com.wanglongxiang.mychat.common.constant.UserConstant;
import com.wanglongxiang.mychat.mapper.CronyGroupMapper;
import com.wanglongxiang.mychat.mapper.CronyMapper;
import com.wanglongxiang.mychat.mapper.UserMapper;
import com.wanglongxiang.mychat.pojo.dto.LoginUserDTO;
import com.wanglongxiang.mychat.pojo.dto.PasswordDTO;
import com.wanglongxiang.mychat.pojo.dto.RegisterUserDTO;
import com.wanglongxiang.mychat.pojo.dto.SearchDTO;
import com.wanglongxiang.mychat.pojo.entity.Crony;
import com.wanglongxiang.mychat.pojo.entity.CronyGroup;
import com.wanglongxiang.mychat.pojo.entity.User;
import com.wanglongxiang.mychat.pojo.vo.CronyGroupVO;
import com.wanglongxiang.mychat.pojo.vo.SearchUserVO;
import com.wanglongxiang.mychat.pojo.vo.UserInfoVO;
import com.wanglongxiang.mychat.service.UserService;
import com.wanglongxiang.mychat.utils.AliossUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    CronyMapper cronyMapper;

    @Autowired
    CronyGroupMapper cronyGroupMapper;

    @Autowired
    AliossUtil aliossUtil;

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
    public ResultPage<SearchUserVO> searchUser(SearchDTO searchDTO, Long userId) {
        ResultPage<SearchUserVO> resultPage = new ResultPage<>();
//        获取查询条件
        String key = searchDTO.getKey();
        Integer size = searchDTO.getSize();
        Integer page = searchDTO.getPage();
        List<User> users = userMapper.searchByUsernameOrNickname(key, page, size,userId, GroupConstant.ROBOTID);
        Integer count = userMapper.searchCountByUsernameOrNickname(key, page, size,userId,GroupConstant.ROBOTID);
//        设置resultPage参数
        resultPage.setPageNum(page/size + 1);
        resultPage.setPageSize(size);
        List<SearchUserVO> searchUserVOS = new ArrayList<>();
        for (User user : users) {
            SearchUserVO searchUserVO = new SearchUserVO();
            Crony crony1 = cronyMapper.selectByUserIdAndCronyId(userId, user.getId());
            Crony crony2 = cronyMapper.selectByUserIdAndCronyId(user.getId(), userId);
//            给searchUserVO赋值
            BeanUtils.copyProperties(user,searchUserVO);
            searchUserVO.setCrony(!(crony1 == null || crony2 == null));
            searchUserVOS.add(searchUserVO);
        }
        resultPage.setData(searchUserVOS);
        resultPage.setTotal(count);
        return resultPage;
    }

    @Override
    public UserInfoVO getOtherInfo(Long userId,Long cronyId) {
        if(cronyId.equals(GroupConstant.ROBOTID)){
            throw new NoPermissionExecption(MessageConstant.ROBOTUNLOOK);
        }

        UserInfoVO userInfoVO = new UserInfoVO();
        User user = userMapper.selectById(cronyId);
        BeanUtils.copyProperties(user,userInfoVO);


//        设置好友分组
        List<CronyGroup> cronyGroups = cronyGroupMapper.findByUid(userId);
//            将cronyGroups转化为cronyGroupVOS
        List<CronyGroupVO> cronyGroupVOS = new ArrayList<>();
        for (CronyGroup cronyGroup : cronyGroups) {
            if(cronyGroup.getCronyGroupName().equals("好友")){
//                设置默认的好友分组，如果是好友下面重新设置
                userInfoVO.setCronyGroupId(cronyGroup.getId());
            }
            CronyGroupVO cronyGroupVO = new CronyGroupVO();
            cronyGroupVO.setCronyGroupId(cronyGroup.getId());
            cronyGroupVO.setCrongGroupName(cronyGroup.getCronyGroupName());
            cronyGroupVOS.add(cronyGroupVO);
        }
        userInfoVO.setCronyGroupVOS(cronyGroupVOS);

        Crony crony = cronyMapper.selectByUserIdAndCronyId(userId, cronyId);
        userInfoVO.setCrony(crony != null);
//        如果是好友
        if(crony != null){
            userInfoVO.setDescription(crony.getDescription());
//            设置分组id
            Long cronyGroupId = crony.getCronyGroupId();
            userInfoVO.setCronyGroupId(cronyGroupId);
        }
//        说明不是好友，设置默认状态返回给前端
        else {
//            备注为用户的昵称
            userInfoVO.setDescription(user.getNickname());
        }
        return userInfoVO;
    }

    @Override
    public String getAvatar(Long userId) {
        User user = userMapper.selectById(userId);
        return user.getAvatar();
    }

    @Override
    public String uploadAvatar(Long userId, MultipartFile file) throws IOException {
        String end = file.getOriginalFilename().split("\\.")[1];
        String uuid = UUID.randomUUID().toString();
        String objectName = new StringBuilder().append(uuid).append(".").append(end).toString();
        String avatar = aliossUtil.upload(file.getBytes(), objectName);
        return avatar;
    }

    @Override
    public User getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        user.setPassword(UserConstant.SHOWPASSWORD);
        return user;
    }

    @Override
    public void updateUser(User user) {
        user.setPassword(null);
        userMapper.updateById(user);
    }

    @Override
    public void updatePassword(Long userId, PasswordDTO passwordDTO) {
        User user = userMapper.selectById(userId);
        if(!user.getPassword().equals(passwordDTO.getOldPassword())){
            throw new PasswordErrorException(MessageConstant.PASSWORDERROR);
        }
        User updUser = new User();
        updUser.setId(userId);
        updUser.setPassword(passwordDTO.getNewPassword());
        userMapper.updateById(updUser);
    }
}
