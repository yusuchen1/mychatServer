package com.wanglongxiang.mychat.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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
