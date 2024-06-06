package com.wanglongxiang.mychat.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Read {
    private Long id;

    @TableField(value = "senduid")
    private Long sendUid;

    @TableField(value = "receiveuid")
    private Long receiveUid;

    @TableField(value = "chatid")
    private Long chatId;
}
