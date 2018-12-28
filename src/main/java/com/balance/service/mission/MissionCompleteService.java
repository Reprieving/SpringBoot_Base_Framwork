package com.balance.service.mission;

import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.constance.MissionConst;
import com.balance.entity.mission.Mission;
import com.balance.entity.mission.MissionComplete;
import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MissionCompleteService extends BaseService {


    /**
     * 新增或更新用户任务完成记录
     *
     * @param userId
     * @param mission 任务实体
     */
    public void createOrUpdateMissionComplete(String userId, Mission mission) {
        if (mission == null) {
            return;
        }
        Map<String, Object> whereMap = ImmutableMap.of(MissionComplete.User_id + "=", userId, MissionComplete.Mission_id + "=", mission.getId());
        MissionComplete missionComplete = selectOneByWhereMap(whereMap, MissionComplete.class);
        // TODO 任务奖励
//        Integer integer;
//        if (missionComplete == null) {
//            missionComplete = new MissionComplete(mission.getId(), userId, mission.getRewardValue(), MissionConst.MISSION_COMPLETE_STATE_FINISH);
//            integer = insert(missionComplete);
//        } else {
//            missionComplete.setStatus(MissionConst.MISSION_COMPLETE_STATE_FINISH);
//            integer = update(missionComplete);
//        }
//        if (integer == 0) {
//            throw new BusinessException("完成任务异常");
//        }
    }
}
