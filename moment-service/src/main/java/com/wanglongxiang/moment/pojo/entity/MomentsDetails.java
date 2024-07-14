package com.wanglongxiang.moment.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MomentsDetails {
    @TableId(type= IdType.AUTO)
    private Long id;
    private Long userId;
    @TableField("`like`")
    private boolean like;
    private Long replay;
    private String content;
    @DateTimeFormat(pattern = "YYYY-MM-DD HH-mm-ss")
    private LocalDateTime time;
    private Long momentsId;
    private String nickname;
    private String replayNickname;

    public boolean getLike() {
        return like;
    }
}
