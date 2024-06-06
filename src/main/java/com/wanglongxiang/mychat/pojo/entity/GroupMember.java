package com.wanglongxiang.mychat.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMember {
    private Long id;

    @TableField(value = "groupid")
    private Long groupId;

    @TableField(value = "memberid")
    private Long memberId;
}
