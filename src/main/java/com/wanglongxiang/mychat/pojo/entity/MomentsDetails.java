package com.wanglongxiang.mychat.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MomentsDetails {
    private Long id;

    @TableField(value = "userid")
    private Long userId;
    @TableField(value = "islike")
    private boolean isLike;
    private Long replay;
    private String context;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;
}
