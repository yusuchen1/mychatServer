package com.wanglongxiang.mychat.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MomentsVO {
    private Long id;
    private Long userId;
    private String avatar;
    private String nickname;
    private String content;
    private List<MomentsLikeVO> likelist;
    private List<MomentsCommentsVO> comments;
    private String input = "";
    private LocalDateTime time;
    private boolean me;
}
