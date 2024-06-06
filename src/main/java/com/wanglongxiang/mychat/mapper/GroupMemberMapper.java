package com.wanglongxiang.mychat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanglongxiang.mychat.pojo.entity.Group;
import com.wanglongxiang.mychat.pojo.entity.GroupMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GroupMemberMapper extends BaseMapper<GroupMember> {
    @Select("select * from group_member where memberid=#{userId};")
    List<GroupMember> getByUid(Long userId);
}
