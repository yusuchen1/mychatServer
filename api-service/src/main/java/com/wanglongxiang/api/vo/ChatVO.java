package com.wanglongxiang.api.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatVO{
    private String id;
    private String avatar;
    private String nickname;
    private Long sendUid;
    private Long receiveUid;
    private Long groupId;
    private LocalDateTime time;
    private String content;
    private boolean isMe;

    public void setId(Long id) {
        this.id = id.toString();
    }
}
