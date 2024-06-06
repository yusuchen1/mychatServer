package com.wanglongxiang.mychat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanglongxiang.mychat.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("select * from user where username = #{username};")
    public User getByUsername(String username);

    public void save(User u);
}
