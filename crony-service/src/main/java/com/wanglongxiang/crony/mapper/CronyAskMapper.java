package com.wanglongxiang.crony.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanglongxiang.crony.pojo.entity.CronyAsk;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CronyAskMapper extends BaseMapper<CronyAsk> {
    public void save(CronyAsk cronyAsk);

    @Select("select * from crony_ask where askid=#{askId} and objid=#{objId};")
    public CronyAsk findByAskIdAndObjId(@Param("askId") Long askId,@Param("objId") Long objId);
    
    @Select("select * from crony_ask where objid = #{userId};")
    public List<CronyAsk> findByObjId(Long userId);

    @Select("select * from crony_ask where askcronygroupid = #{groupId};")
    public List<CronyAsk> findByAskCronyGroupId(Long groupId);
}
