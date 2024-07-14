package com.wanglongxiang.moment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanglongxiang.moment.pojo.entity.MomentsDetails;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MomentDetailsMapper extends BaseMapper<MomentsDetails> {
    @Select("select * from moments_details where moments_id = #{momentId} ;")
    List<MomentsDetails> selectByMomentId(@Param("momentId") Long momentId);

    @Delete("delete from moments_details where moments_id = #{momentId};")
    void deleteByMomentsId(@Param("momentId") Long momentId);

    @Update("update moments_details set nickname = #{nickname} where user_id = #{userId};")
    void updateNickname(@Param("userId") Long userId,@Param("nickname") String nickname);

    @Update("update moments_details set replay_nickname = #{replayNickname} where replay = #{replay};")
    void updateReplayNickname(@Param("replay") Long replay,@Param("replayNickname") String replayNickname);
}
