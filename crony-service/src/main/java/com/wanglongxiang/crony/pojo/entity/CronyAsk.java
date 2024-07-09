package com.wanglongxiang.crony.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CronyAsk {
    private Long id;

    @TableField("askid")
    private Long askId;

    @TableField("askcronygroupid")
    private Long askCronyGroupId;

    @TableField("objid")
    private Long objId;

    private String description;
}
