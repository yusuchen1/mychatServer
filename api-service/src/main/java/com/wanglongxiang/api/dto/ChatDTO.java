package com.wanglongxiang.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO {
    private Long receiveUid;
    private Long groupId;
    private String content;
}
