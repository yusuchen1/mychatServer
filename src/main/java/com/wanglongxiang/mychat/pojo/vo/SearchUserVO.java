package com.wanglongxiang.mychat.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchUserVO {
    private Long id;
    private String avatar;
    private String nickname;
    private String username;
    private String signature;
    private boolean isCrony;
}