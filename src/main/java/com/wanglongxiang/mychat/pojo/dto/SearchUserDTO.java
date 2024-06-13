package com.wanglongxiang.mychat.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchUserDTO {
    private String key;
    private Integer page;
    private Integer size;
}






