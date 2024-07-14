package com.wanglongxiang.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MomentCommontDTO {
    private Long replay;
    private String content;
    private Long momentsId;
}
