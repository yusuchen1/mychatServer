package com.wanglongxiang.mychat.pojo.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatVO{
    private Long id;
    private String avatar;
    private String nickname;
    private Long sendUid;
    private Long receiveUid;
    private Long groupId;
    private LocalDateTime time;
    private String content;
    private boolean isMe;
}
