package com.wanglongxiang.user.service.impl;

import com.wanglongxiang.api.client.CronyClient;
import com.wanglongxiang.api.client.MomentClient;
import com.wanglongxiang.api.dto.LoginUserDTO;
import com.wanglongxiang.api.dto.PasswordDTO;
import com.wanglongxiang.api.dto.RegisterUserDTO;
import com.wanglongxiang.api.dto.SearchDTO;
import com.wanglongxiang.api.vo.*;
import com.wanglongxiang.mychat.common.ResultPage;
import com.wanglongxiang.mychat.common.constant.*;
import com.wanglongxiang.mychat.exception.NoPermissionExecption;
import com.wanglongxiang.user.exception.UserException.PasswordErrorException;
import com.wanglongxiang.user.exception.UserException.UserExistException;
import com.wanglongxiang.user.exception.UserException.UserNotFoundException;
import com.wanglongxiang.user.mapper.UserMapper;
import com.wanglongxiang.user.pojo.entity.User;
import com.wanglongxiang.user.service.UserService;
import com.wanglongxiang.user.utils.AliossUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    CronyClient cronyClient;

    @Autowired
    MomentClient momentClient;

    @Autowired
    AliossUtil aliossUtil;

    @Autowired
    RabbitTemplate rabbitTemplate;

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
    public void register(RegisterUserDTO registerUserDTO) {
        User user = new User();
        BeanUtils.copyProperties(registerUserDTO,user);
        user.setAvatar(UserConstant.defaultAvatar);
        User u = userMapper.getByUsername(user.getUsername());
        if(u != null){
            throw new UserExistException(MessageConstant.USEREXIST);
        }
        userMapper.save(user);

        boolean b = cronyClient.addCronyGroupByUid(user.getId(), CronyConstant.DEFAULTCRONYGROUP);
        if(!b){
            throw new RuntimeException();
        }
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
            BeanUtils.copyProperties(user,searchUserVO);
//            给searchUserVO赋值
            searchUserVO.setCrony(cronyClient.isCrony(userId, user.getId()));
            searchUserVOS.add(searchUserVO);
        }
        resultPage.setData(searchUserVOS);
        resultPage.setTotal(count);
        return resultPage;
    }

    @Override
    public UserInfoVO getOtherInfo(Long userId, Long cronyId) {
        if(cronyId.equals(GroupConstant.ROBOTID)){
            throw new NoPermissionExecption(MessageConstant.ROBOTUNLOOK);
        }

        User user = userMapper.selectById(cronyId);
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(user,userInfoVO);

//        根据id获取到好友分组集
        List<CronyGroupVO> cronyGroupVOS = cronyClient.getCronyGroupVOS();
        cronyGroupVOS.forEach(cronyGroupVO -> {
            if(cronyGroupVO.getCrongGroupName().equals("好友")){
//                设置默认的好友分组，如果是好友下面重新设置
                userInfoVO.setCronyGroupId(cronyGroupVO.getCronyGroupId());
            }
        });

        userInfoVO.setCronyGroupVOS(cronyGroupVOS);


        userInfoVO.setCrony(cronyClient.isCrony(userId, cronyId));
        CronyDesAndCGidVO cronyDesAndCGidVO = cronyClient.getCronyDesAndCGidVO(userId,cronyId);
////        如果是好友
        if(cronyDesAndCGidVO != null){
            userInfoVO.setDescription(cronyDesAndCGidVO.getDescription());
//            设置分组id
            Long cronyGroupId = cronyDesAndCGidVO.getCronyGroupId();
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
    @Cacheable(cacheNames = "avatar",key = "#userId")
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
    @Cacheable(cacheNames = "userInfo",key = "#userId")
    public User getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        user.setPassword(UserConstant.SHOWPASSWORD);
        return user;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"avatar","userInfo"},key = "#user.id")
    public void updateUser(User user) {
        user.setPassword(null);
        User u = userMapper.selectById(user.getId());
        userMapper.updateById(user);
        if(!u.getNickname().equals(user.getNickname())){
            HashMap<Long,String> map = new HashMap<>();
            map.put(user.getId(),user.getNickname());
            rabbitTemplate.convertAndSend(MQConstant.EXCHANGE_UPDATEUSER,MQConstant.KEY_UPDATEUSER,map);
        }
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

    @Override
    public UserChatInfoVO getUserChatInfo(Long uid) {
        User user = userMapper.selectById(uid);
        if(user == null){
            return null;
        }
        UserChatInfoVO userChatInfoVO = new UserChatInfoVO();
        BeanUtils.copyProperties(user,userChatInfoVO);
        return userChatInfoVO;
    }

    @Override
    public List<SearchUserVO> getSearchUserVOSByUids(Long userId,List<Long> uids) {
        List<User> users = userMapper.selectBatchIds(uids);
        List<SearchUserVO> searchUserVOS = new ArrayList<>();
        for (User user : users) {
            SearchUserVO searchUserVO = new SearchUserVO();;
            searchUserVO.setCrony(cronyClient.isCrony(userId, user.getId()));
//            给searchUserVO赋值
            BeanUtils.copyProperties(user,searchUserVO);
            searchUserVOS.add(searchUserVO);
        }
        return searchUserVOS;
    }

    @Override
    public List<UserChatInfoVO> getUserChatInfos(List<Long> uids) {
        if(uids == null || uids.size() == 0){
            return new ArrayList<>();
        }
        List<User> users = userMapper.selectBatchIds(uids);
        if(users == null || users.size() == 0){
            return new ArrayList<>();
        }
        List<UserChatInfoVO> userChatInfoVOS = users.stream().map(user -> {
            UserChatInfoVO userChatInfoVO = new UserChatInfoVO();
            BeanUtils.copyProperties(user, userChatInfoVO);
            return userChatInfoVO;
        }).collect(Collectors.toList());

        return userChatInfoVOS;
    }
}
