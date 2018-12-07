package com.balance.mapper.mission;

import com.balance.entity.mission.Mission;
import com.balance.entity.mission.MissionComplete;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MissionMapper {
    Integer selectCountTodaySign(@Param("userId")String userId, @Param("today")String today);
}
