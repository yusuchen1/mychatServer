package com.wanglongxiang.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMVO {
    private Long id;
    private String avatar;
    private String name;
    private String number;
}
