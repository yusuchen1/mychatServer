package com.wanglongxiang.chat.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
    private Long id;

    @TableField(value = "sendUid")
    private Long sendUid;

    @TableField(value = "receiveuid")
    private Long receiveUid;

    @TableField(value = "groupid")
    private Long groupId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

    private String content;
}
