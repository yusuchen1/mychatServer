package com.wanglongxiang.crony.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Crony {
    private Long id;

    @TableField(value = "userid")
    private Long userId;

    @TableField(value = "cronyid")
    private Long cronyId;

    @TableField(value = "crony_group_id")
    private Long cronyGroupId;

    private String description;

    public Crony(Long userId, Long cronyId, Long cronyGroupId, String description) {
        this.userId = userId;
        this.cronyId = cronyId;
        this.cronyGroupId = cronyGroupId;
        this.description = description;
    }
}
