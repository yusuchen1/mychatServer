package com.wanglongxiang.user.service;

import com.wanglongxiang.api.dto.LoginUserDTO;
import com.wanglongxiang.api.dto.PasswordDTO;
import com.wanglongxiang.api.dto.RegisterUserDTO;
import com.wanglongxiang.api.dto.SearchDTO;
import com.wanglongxiang.api.vo.SearchUserVO;
import com.wanglongxiang.api.vo.UserChatInfoVO;
import com.wanglongxiang.api.vo.UserInfoVO;
import com.wanglongxiang.mychat.common.ResultPage;
import com.wanglongxiang.user.pojo.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    User login(LoginUserDTO loginUserDTO);
    void register(RegisterUserDTO registerUserDTO);
    ResultPage<SearchUserVO> searchUser(SearchDTO searchDTO, Long userId);
    UserInfoVO getOtherInfo(Long userId, Long cronyId);

    String getAvatar(Long userId);

    String uploadAvatar(Long userId, MultipartFile file) throws IOException;

    User getUserInfo(Long userId);

    void updateUser(User user);

    void updatePassword(Long userId, PasswordDTO passwordDTO);

    UserChatInfoVO getUserChatInfo(Long uid);

    List<SearchUserVO> getSearchUserVOSByUids(Long userId,List<Long> uids);

    List<UserChatInfoVO> getUserChatInfos(List<Long> uids);
}
