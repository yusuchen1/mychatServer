package com.wanglongxiang.mychat.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CronyGroup {
    private Long id;

    @TableField(value = "userid")
    private Long userId;

    @TableField(value = "cronygroupname")
    private String cronyGroupName;
}
