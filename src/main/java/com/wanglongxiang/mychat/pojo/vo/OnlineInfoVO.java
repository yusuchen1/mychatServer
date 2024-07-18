package com.wanglongxiang.mychat.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlineInfoVO {
    private Long userId;
    private Boolean online;
}
