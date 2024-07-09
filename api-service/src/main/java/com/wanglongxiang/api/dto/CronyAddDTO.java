package com.wanglongxiang.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CronyAddDTO {
    private Long CronyAskId;
    private Long CronyGroupId;
    private String description;
}
