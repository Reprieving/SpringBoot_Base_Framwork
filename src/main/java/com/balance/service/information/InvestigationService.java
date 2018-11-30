package com.balance.service.information;

import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.constance.MissionConst;
import com.balance.entity.information.Investigation;
import com.balance.entity.mission.Mission;
import com.balance.service.mission.MissionCompleteService;
import com.balance.service.mission.MissionService;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InvestigationService extends BaseService{

    @Autowired
    private MissionService missionService;

    @Autowired
    private MissionCompleteService missionCompleteService;


    /**
     * 按美妆品id查询调查模板
     * @param beautyId
     * @return
     */
    public List<Investigation> listInvestigationTemplate(String beautyId){
        Map<String,Object> whereMap = ImmutableMap.of(Investigation.Beauty_id,beautyId,Investigation.Is_template,1);
        List<Investigation> investigationTemplates = selectListByWhereMap(whereMap,null,Investigation.class);
        return investigationTemplates;
    }

    /**
     * 提交问卷调查
     * @param userId
     * @param investigation
     */
    public void createInvestigation(String userId,Investigation investigation){
        investigation.setUserId(userId);
        investigation.setIsTemplate(false);
        Integer i = insert(investigation);
        if (i == 0) {
            throw new BusinessException("提交问卷失败");
        }

        //完成任务
        Mission mission = missionService.filterTaskByCode(MissionConst.JOIN_INVESTIGATION, missionService.selectAll(null, Mission.class));
        missionCompleteService.createOrUpdateMissionComplete(userId, mission);
    }

}
