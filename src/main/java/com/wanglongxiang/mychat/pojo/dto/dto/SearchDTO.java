package com.wanglongxiang.mychat.pojo.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchDTO {
    private String key;
    private Integer page;
    private Integer size;
}






