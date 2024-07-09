package com.wanglongxiang.mychat.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultPage<T> {
    private List<T> data;
    private Integer pageSize;
    private Integer total;
    private Integer pageNum;
}
