package com.wanglongxiang.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MomentsCommentsVO {
    private Long id;
    private Long userId;
    private String nickname;
    private String content;
    private Long replay;
    private String replayNickname;
}
