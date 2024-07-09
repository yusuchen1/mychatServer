package com.wanglongxiang.crony.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
