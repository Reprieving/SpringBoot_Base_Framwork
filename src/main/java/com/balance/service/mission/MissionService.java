package com.balance.service.mission;

import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.architecture.utils.ValueCheckUtils;
import com.balance.constance.AssetTurnoverConst;
import com.balance.constance.MissionConst;
import com.balance.constance.UserConst;
import com.balance.entity.mission.Mission;
import com.balance.entity.mission.MissionComplete;
import com.balance.entity.user.User;
import com.balance.entity.user.UserAssets;
import com.balance.service.user.AssetsTurnoverService;
import com.balance.service.user.UserAssetsService;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.*;

@Service
public class MissionService extends BaseService {

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private MissionCompleteService missionCompleteService;

    @Autowired
    private UserAssetsService userAssetsService;

    @Autowired
    private AssetsTurnoverService assetsTurnoverService;


    /**
     * 查询任务列表信息
     *
     * @return
     */
    public List<Mission> getMissionList() {
        return selectAll(null, Mission.class);
    }

    /**
     * 领取任务奖励
     *
     * @param missionCompleteId
     * @param userId
     */
    public void obtainMissionReward(String missionCompleteId, String userId) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                MissionComplete missionComplete = selectOneById(missionCompleteId, MissionComplete.class);

                ValueCheckUtils.notEmpty(missionComplete, "未找到个人任务");

                Mission mission = filterTaskById(missionComplete.getMissionId(), selectAll(null, Mission.class));
                ValueCheckUtils.notEmpty(mission, "未找到任务信息");

                if (missionComplete.getStatus() != MissionConst.MISSION_COMPLETE_STATE_FINISH) {
                    throw new BusinessException("未完成任务");
                }

                /**
                 * 1.更新个人任务状态为已领取奖励
                 * 2.按任务奖励方式增加用户资产
                 * 3.添加活动记录
                 */
                if (mission.getDisposable() == 1 || checkIfSignTask(mission.getTaskCode())) {//一次性任务 和签到任务 更新为已领取
                    MissionComplete missionComplete1 = new MissionComplete(missionCompleteId, MissionConst.MISSION_COMPLETE_STATE_RECEIVE);
                    updateIfNotNull(missionComplete1);
                } else {//非一次性任务 更新为未完成
                    MissionComplete missionComplete1 = new MissionComplete(missionCompleteId, MissionConst.MISSION_COMPLETE_STATE_NONE);
                    updateIfNotNull(missionComplete1);
                }

                //按任务奖励方式增加用户资产
                BigDecimal rewardValue = missionComplete.getRewardAmount();
                Integer settlementId = mission.getSettlementId();

                //更改资产数目
                UserAssets userAssets = userAssetsService.getAssetsByUserId(userId);
                userAssetsService.changeUserAssets(userId, rewardValue, settlementId, userAssets);

                //增加流水记录
                assetsTurnoverService.createAssetsTurnover(userId, AssetTurnoverConst.TURNOVER_TYPE_MISSION_REWARD, rewardValue, AssetTurnoverConst.COMPANY_ID, userId, userAssets, settlementId, "领取" + mission.getTaskName() + "奖励");

            }
        });
    }

    /**
     * 完成任务并领取奖励
     *
     * @param user         用户实体
     * @param missionCode  任务编码
     * @param turnoverDesc 流水描述
     */
    public void finishMission(User user, Integer missionCode, String turnoverDesc) {
        String userId = user.getId();
        Mission mission = filterTaskByCode(missionCode, selectAll(null, Mission.class));
        UserAssets userAssets = userAssetsService.getAssetsByUserId(userId);
        BigDecimal rewardValue = mission.getRewardValue();

        if(user.getMemberType()== UserConst.USER_MEMBER_TYPE_COMMON && mission.getMemberRewardValue()!=null){
            rewardValue = mission.getMemberRewardValue();
        }

        Integer settlementId = mission.getSettlementId();
        userAssetsService.changeUserAssets(userId, rewardValue, settlementId, userAssets);
        assetsTurnoverService.createAssetsTurnover(
                userId, AssetTurnoverConst.TURNOVER_TYPE_MISSION_REWARD, rewardValue, AssetTurnoverConst.COMPANY_ID, userId, userAssets, settlementId, turnoverDesc
        );
    }

    /**
     * 完成任务并领取奖励
     *
     * @param userId         用户实体
     * @param missionCode  任务编码
     * @param turnoverDesc 流水描述
     */
    public void finishMission(String userId, Integer missionCode, String turnoverDesc) {
        Mission mission = filterTaskByCode(missionCode, selectAll(null, Mission.class));
        UserAssets userAssets = userAssetsService.getAssetsByUserId(userId);
        BigDecimal rewardValue = mission.getRewardValue();

        Integer settlementId = mission.getSettlementId();
        userAssetsService.changeUserAssets(userId, rewardValue, settlementId, userAssets);
        assetsTurnoverService.createAssetsTurnover(
                userId, AssetTurnoverConst.TURNOVER_TYPE_MISSION_REWARD, rewardValue, AssetTurnoverConst.COMPANY_ID, userId, userAssets, settlementId, turnoverDesc
        );
    }



    /**
     * 根据编码筛选任务
     *
     * @param code
     * @param missions
     * @return
     */
    public Mission filterTaskByCode(Integer code, List<Mission> missions) {
        Mission filterMission = null;
        if (missions == null) {
            throw new RuntimeException("任务列表为空");
        }
        for (Mission mission : missions) {
            if (code == (mission.getTaskCode())) {
                filterMission = mission;
            }
        }
        return filterMission;
    }

    /**
     * 根据个人任务id筛选任务
     *
     * @param missionCompleteId
     * @param missions
     * @return
     */
    public Mission filterTaskById(String missionCompleteId, List<Mission> missions) {
        if (missions == null) {
            throw new RuntimeException("任务列表为空");
        }
        for (Mission mission : missions) {
            if (missionCompleteId == (mission.getId())) {
                return mission;
            }
        }
        return null;
    }

    /**
     * 根据任务编码判断是否为签到任务
     *
     * @param taskCode
     * @return
     */
    public Boolean checkIfSignTask(Integer taskCode) {
        if (taskCode == MissionConst.SIGN_DAY || taskCode == MissionConst.SIGN_WEEK || taskCode == MissionConst.SIGN_MONTH) {
            return true;
        } else {
            return false;
        }
    }
}
