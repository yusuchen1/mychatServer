package com.wanglongxiang.mychat.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Moments {
    @TableId(type= IdType.AUTO)
    private Long id;
    private Long userId;
    private String content;
    @DateTimeFormat(pattern = "YYYY-MM-dd HH-mm-ss")
    private LocalDateTime time;
    private String avatar;
    private String nickname;
}
