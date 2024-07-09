package com.wanglongxiang.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchGroupVO {
    private Long id;
    private String avatar;
    private String number;
    private String name;
    private boolean isInclude;
}
