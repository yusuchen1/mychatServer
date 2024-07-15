package com.wanglongxiang.mychat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanglongxiang.mychat.pojo.entity.Chat;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChatMapper extends BaseMapper<Chat> {
    List<Chat> selectBySidAndRid(@Param("sid") Long sid, @Param("rid") Long rid);
    
    @Select("select * from chat where groupid = #{gid} ORDER BY time;")
    List<Chat> selectByGid(Long gid);

    @Delete("delete from chat where groupid = #{gid};")
    void deleteByGid(Long gid);
}
