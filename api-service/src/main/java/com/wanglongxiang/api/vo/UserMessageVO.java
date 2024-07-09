package com.wanglongxiang.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMessageVO {
    private Long AskId;
    private Long id;
    private String avatar;
    private String nickname;
    private String signature;
}
