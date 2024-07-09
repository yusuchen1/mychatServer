package com.wanglongxiang.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgreeCronyVO {
    private Long cronyAskId;
    private Long cronyGroupId;
    private String description;
}
