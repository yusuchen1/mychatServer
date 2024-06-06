package com.wanglongxiang.mychat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanglongxiang.mychat.pojo.entity.Group;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GroupMapper extends BaseMapper<Group> {

}
