package com.wanglongxiang.crony.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanglongxiang.crony.pojo.entity.CronyGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CronyGroupMapper extends BaseMapper<CronyGroup> {
    public void save(CronyGroup cronyGroup);
    
    @Select("select * from crony_group where userid=#{uid} and cronygroupname=#{cronyGroupName};")
    public CronyGroup findByUidAndGroupName(@Param("uid") Long uid,@Param("cronyGroupName") String cronyGroupName);

    @Select("select * from crony_group where userid=#{userId};")
    public List<CronyGroup> findByUid(Long userId);
}
