package com.wanglongxiang.mychat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanglongxiang.mychat.pojo.entity.Crony;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CronyMapper extends BaseMapper<Crony> {
    void save(Crony crony);

    @Select("select * from crony where crony_group_id=#{cronyGroupId};")
    public List<Crony> findByGroupId(Long cronyGroupId);
    
    @Select("select * from crony where userid=#{userId} and cronyid=#{cronyId};")
    public Crony selectByUserIdAndCronyId(@Param("userId") Long userId,@Param("cronyId") Long cronyId);

    void updateCronyInfo(@Param("userId") Long userId,
                         @Param("cronyId") Long cronyId,
                         @Param("description")String description,
                         @Param("cronyGroupId")Long cronyGroupId);

    @Delete("delete from crony where userid = #{userId} and cronyid = #{cronyId};")
    void deleteByUidAndCid(@Param("userId") Long userId,@Param("cronyId") Long cronyId);

}
