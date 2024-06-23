package com.wanglongxiang.mychat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanglongxiang.mychat.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("select * from user where username = #{username};")
    User getByUsername(String username);

    void save(User u);

    List<User> searchByUsernameOrNickname(@Param("key") String key,@Param("page") Integer page,@Param("size") Integer size,@Param("userId") Long userId,@Param("robotId") Long robotId);

    Integer searchCountByUsernameOrNickname(@Param("key") String key,@Param("page") Integer page,@Param("size") Integer size,@Param("userId") Long userId,@Param("robotId") Long robotId);

}
