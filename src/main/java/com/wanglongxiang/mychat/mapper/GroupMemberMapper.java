package com.wanglongxiang.mychat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanglongxiang.mychat.pojo.entity.GroupMember;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GroupMemberMapper extends BaseMapper<GroupMember> {
    @Select("select * from group_member where memberid=#{userId};")
    List<GroupMember> getByUid(Long userId);

    void save(GroupMember groupMember);

    @Select("select * from group_member where groupid = #{gid};")
    List<GroupMember> getByGid(Long gid);

    @Select("select * from group_member where groupid = #{gid} and memberid = #{uid}")
    GroupMember getByGidAndUid(@Param("gid") Long gid,@Param("uid") Long uid);

    @Delete("delete from group_member where memberid = #{uid} and groupid = #{gid}")
    void deleteByUidAndGid(@Param("uid") Long uid,@Param("gid") Long gid);
}