package com.wanglongxiang.mychat.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LChatVO {
    private Long uid;
    private Long gid;
    private List<ChatVO> chats;
}
