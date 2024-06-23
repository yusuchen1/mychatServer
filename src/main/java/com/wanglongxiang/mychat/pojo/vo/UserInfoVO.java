package com.wanglongxiang.mychat.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoVO {
    private Long id;
    private String avatar;
    private String nickname;
    private String signature;
    private String username;
    private String sex;
    private String address;
    private String phone;
    private String description;
    private List<CronyGroupVO> cronyGroupVOS;
    private Long cronyGroupId;
    private boolean isCrony;
}
