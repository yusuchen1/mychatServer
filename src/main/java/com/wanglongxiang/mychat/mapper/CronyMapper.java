package com.wanglongxiang.mychat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanglongxiang.mychat.pojo.entity.Crony;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CronyMapper extends BaseMapper<Crony> {
    public void save(Crony crony);

    @Select("select * from crony where crony_group_id=#{cronyGroupId};")
    public List<Crony> findByGroupId(Long cronyGroupId);
}
