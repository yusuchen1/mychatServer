package com.wanglongxiang.mychat.pojo.other;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CronyGroupList {
    private String groupName;
    List<CronyListItem> cronys;
}
