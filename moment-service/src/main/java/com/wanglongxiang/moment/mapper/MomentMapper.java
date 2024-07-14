package com.wanglongxiang.moment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanglongxiang.moment.pojo.entity.Moments;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface MomentMapper extends BaseMapper<Moments> {
    @Update("update moments set content = #{content} where id = #{id};")
    void updateContent(@Param("id") Long id,@Param("content") String content);

    @Select("select * from moments where user_id = #{cronyId};")
    List<Moments> selectByUserId(Long cronyId);

    @Update("update moments set nickname = #{nickname} where user_id = #{userId};")
    void updateNickname(@Param("userId") Long userId,@Param("nickname") String nickname);
}
