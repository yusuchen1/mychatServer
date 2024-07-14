package com.wanglongxiang.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommontNicknameVO {
    private Long momentDetailsId;
    private Long userId;
    private String nickname;
    private String replayNickname;
}
