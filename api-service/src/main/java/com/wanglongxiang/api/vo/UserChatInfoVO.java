package com.wanglongxiang.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserChatInfoVO {
    private Long id;
    private String nickname;
    private String avatar;
    private String signature;
}
