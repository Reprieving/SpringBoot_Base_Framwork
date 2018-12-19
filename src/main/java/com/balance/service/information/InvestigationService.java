package com.balance.service.information;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.constance.MissionConst;
import com.balance.entity.information.Investigation;
import com.balance.entity.mission.Mission;
import com.balance.mapper.information.InvestigationMapper;
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

    @Autowired
    private InvestigationMapper investigationMapper;


    /**
     * 按美妆品id查询调查模板
     * @param beautyId
     * @return
     */
    public List<Investigation> listInvestigationTemplate(String beautyId, Pagination pagination){
        Map<String,Object> whereMap = ImmutableMap.of(Investigation.Template_id+"=","0", Investigation.Beauty_id+"=",beautyId);
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
                // TODO 问卷模板ID
//                investigation.setIsTemplate(false);
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

    public Pagination getByPage(Map<String, Object> params) {
        Object templateId = params.get("templateId");
        List<Investigation> investigationList;
        int total;
        if ("0".equals(templateId)) {
            investigationList = investigationMapper.selectPageWithGoods(params);
            total = investigationMapper.selectCountWithGoods(params);
        } else {
            investigationList = investigationMapper.selectPageWithUser(params);
            total = investigationMapper.selectCountWithUser(params);
        }
        Pagination pagination = new Pagination();
        pagination.setObjectList(investigationList);
        pagination.setTotalRecordNumber(total);
        return pagination;
    }
}
