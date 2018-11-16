package com.balance.mapper.mission;

import com.balance.entity.applet.Mission;
import com.balance.entity.applet.MissionComplete;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MissionMapper {
    List<Mission> selectMissionList(Integer type);

    MissionComplete selectCompletion(String id, String userId);
}
