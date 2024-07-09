package com.wanglongxiang.group.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanglongxiang.group.pojo.entity.Group;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface GroupMapper extends BaseMapper<Group> {
    void save(Group group);

    @Select("select * from `group` where number = #{number};")
    Group findByNumber(String number);

    List<Group> getByIds(@Param("groupIds") List<Long> groupIds);

    List<Group> searchByNameOrNumber(@Param("key") String key
            ,@Param("page") Integer page
            ,@Param("size") Integer size);

    Integer searchCountByNameOrNumber(@Param("key") String key
            ,@Param("page") Integer page
            ,@Param("size") Integer size);
    
    @Select("select * from `group` where id = #{id};")
    Group getById(Long id);
    
    @Select("select * from `group` where makeuid = #{userId};")
    List<Group> getByUid(Long userId);

    @Update("update `group` set num = #{num} where id = #{id};")
    void updateNum(@Param("id") Long id,@Param("num") int num);
}
