package com.wanglongxiang.mychat.pojo.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCronyInfoDTO {
    private Long cronyId;
    private Long cronyGroupId;
    private String description;
}
