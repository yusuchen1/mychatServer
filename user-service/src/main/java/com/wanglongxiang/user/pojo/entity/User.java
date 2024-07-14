package com.wanglongxiang.user.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("`user`")
public class User {
    private Long id;
    private String avatar;
    private String nickname;
    private String signature;
    private String username;
    private String password;
    private String sex;
    private String address;
    private String phone;
}
