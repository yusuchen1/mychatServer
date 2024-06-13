package com.wanglongxiang.mychat.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group {
    private Long id;

    @TableField(value = "groupavatar")
    private String groupAvater;

    @TableField(value = "group_number")
    private Long groupNumber;

    @TableField(value = "groupname")
    private String groupName;

    @TableField(value = "groupnum")
    private Integer groupNum;
}
