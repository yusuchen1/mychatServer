package com.wanglongxiang.mychat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanglongxiang.mychat.pojo.entity.CronyAsk;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CronyAskMapper extends BaseMapper<CronyAsk> {
    public void save(CronyAsk cronyAsk);

    @Select("select * from crony_ask where askid=#{askId} and objid=#{objId};")
    public CronyAsk findByAskIdAndObjId(@Param("askId") Long askId,@Param("objId") Long objId);
}
