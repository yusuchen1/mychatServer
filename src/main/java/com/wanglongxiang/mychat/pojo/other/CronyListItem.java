package com.wanglongxiang.mychat.pojo.other;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CronyListItem {
    private Long id;
    private String avatar;
    private String name;
    private String nmessage;
}
