package com.wanglongxiang.mychat.service;

import com.wanglongxiang.mychat.common.ResultPage;
import com.wanglongxiang.mychat.pojo.dto.LoginUserDTO;
import com.wanglongxiang.mychat.pojo.dto.PasswordDTO;
import com.wanglongxiang.mychat.pojo.dto.RegisterUserDTO;
import com.wanglongxiang.mychat.pojo.dto.SearchDTO;
import com.wanglongxiang.mychat.pojo.entity.User;
import com.wanglongxiang.mychat.pojo.vo.SearchUserVO;
import com.wanglongxiang.mychat.pojo.vo.UserInfoVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    User login(LoginUserDTO loginUserDTO);
    User register(RegisterUserDTO registerUserDTO);
    ResultPage<SearchUserVO> searchUser(SearchDTO searchDTO, Long userId);
    UserInfoVO getOtherInfo(Long userId,Long cronyId);

    String getAvatar(Long userId);

    String uploadAvatar(Long userId, MultipartFile file) throws IOException;

    User getUserInfo(Long userId);

    void updateUser(User user);

    void updatePassword(Long userId, PasswordDTO passwordDTO);
}
