package com.wanglongxiang.mychat.mapper;

import com.wanglongxiang.mychat.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from user where username = #{username};")
    public User getByUsername(String username);

    public void save(User u);
}
