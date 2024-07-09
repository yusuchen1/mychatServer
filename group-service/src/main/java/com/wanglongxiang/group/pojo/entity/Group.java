package com.wanglongxiang.group.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("`group`")
public class Group {
    private Long id;

    @TableField(value = "avatar")
    private String avatar;

    @TableField(value = "number")
    private String number;

    @TableField(value = "name")
    private String name;

    @TableField(value = "num")
    private Integer num;

    @TableField(value = "makeuid")
    private Long makeUid;

    @TableField(value = "makeusername")
    private String makeUsername;
}
