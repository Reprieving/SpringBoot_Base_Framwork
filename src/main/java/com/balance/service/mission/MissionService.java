package com.balance.service.mission;

import com.balance.architecture.service.BaseService;
import com.balance.constance.MissionConst;
import com.balance.entity.applet.Mission;
import com.balance.entity.applet.MissionComplete;
import com.balance.entity.applet.SignInfo;
import com.balance.mapper.mission.MissionMapper;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MissionService extends BaseService<Mission>{

    @Autowired
    private MissionCompleteService missionCompleteService;

    @Autowired
    private MissionMapper missionMapper;

    /**
     * 查询任务列表信息
     * @param userId
     * @param type
     * @return
     */
    public List<Mission> getMissionList(String userId, Integer type) {
        List<Mission> missions = selectListByWhereString("type=",type,null,Mission.class);
        for (Mission mission : missions) {

            Map<String,Object> paramMap = ImmutableMap.of("mission_id=",mission.getId(),"user_id=",userId);
            MissionComplete missionComplete = missionCompleteService.selectOneByWhereMap(paramMap,MissionComplete.class);

            Boolean missionCompleteNull = missionComplete == null;

            if (missionCompleteNull) {
                mission.setState(MissionConst.MISSION_COMPLETE_STATE_NONE);
            } else {
                mission.setState(missionComplete.getStatus());
            }

            if(missionComplete!=null){
                mission.setMissionCompleteId(missionComplete.getId());
            }
        }
        return missions;
    }

    /**
     * 领取任务奖励
     * @param missionCompleteId
     * @param userId
     */
    public void obtainMissionReward(String missionCompleteId, String userId) {

    }

    /**
     * 签到
     * @param userId
     */
    public void signIn(String userId) {

    }

    /**
     * 获取签到列表信息
     * @param userId
     * @return
     */
    public SignInfo getSignList(String userId) {
        return null;
    }
}
