package com.wanglongxiang.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CronyDesAndCGidVO {
    private Long userId;
    private String description;
    private Long cronyGroupId;
}
