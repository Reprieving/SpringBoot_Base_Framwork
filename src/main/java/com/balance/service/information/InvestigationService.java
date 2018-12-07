package com.balance.service.information;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.constance.MissionConst;
import com.balance.entity.information.Investigation;
import com.balance.entity.mission.Mission;
import com.balance.service.mission.MissionCompleteService;
import com.balance.service.mission.MissionService;
import com.google.common.collect.ImmutableMap;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Map;

@Service
public class InvestigationService extends BaseService{

    @Autowired
    private MissionService missionService;

    @Autowired
    private MissionCompleteService missionCompleteService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    /**
     * 按美妆品id查询调查模板
     * @param beautyId
     * @return
     */
    public List<Investigation> listInvestigationTemplate(String beautyId, Pagination pagination){
        Map<String,Object> whereMap = ImmutableMap.of(Investigation.Beauty_id+"=",beautyId,Investigation.Is_template+"=",true);
        List<Investigation> investigationTemplates = selectListByWhereMap(whereMap,pagination,Investigation.class);
        return investigationTemplates;
    }

    /**
     * 提交问卷调查
     * @param userId
     * @param investigation
     */
    public void createInvestigation(String userId,Investigation investigation){
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                investigation.setUserId(userId);
                investigation.setIsTemplate(false);
                Integer i = insertIfNotNull(investigation);
                if (i == 0) {
                    throw new BusinessException("提交问卷失败");
                }

                //完成任务
                Mission mission = missionService.filterTaskByCode(MissionConst.JOIN_INVESTIGATION, missionService.selectAll(null, Mission.class));
                missionCompleteService.createOrUpdateMissionComplete(userId, mission);
            }
        });

    }

}
