package com.balance.service.mission;

import com.alibaba.fastjson.JSONObject;
import com.balance.architecture.dto.Pagination;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.client.RedisClient;
import com.balance.constance.*;
import com.balance.entity.mission.Mission;
import com.balance.entity.mission.MissionComplete;
import com.balance.entity.mission.MissionDescription;
import com.balance.entity.mission.MissionReward;
import com.balance.entity.user.User;
import com.balance.entity.user.UserAssets;
import com.balance.service.common.GlobalConfigService;
import com.balance.service.user.AssetsTurnoverService;
import com.balance.service.user.CertificationService;
import com.balance.service.user.UserAssetsService;
import com.balance.service.user.UserService;
import com.balance.utils.BigDecimalUtils;
import com.balance.utils.MineDateUtils;
import com.balance.utils.ValueCheckUtils;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 任务
 */
@Service
public class MissionService extends BaseService {

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private UserAssetsService userAssetsService;

    @Autowired
    private AssetsTurnoverService assetsTurnoverService;

    @Autowired
    private UserService userService;

    @Autowired
    private SignInService signInService;

    @Autowired
    private CertificationService certificationService;

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private GlobalConfigService globalConfigService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 有效 的任务列表
     */
    public List<Mission> getValidList(String userId) {
        List<Mission> missions = getValidList();
        for (int i = 0; i < missions.size(); i++) {
            Mission mission = missions.get(i);
            if (!mission.getDisplay()) {
                missions.remove(i--);
                continue;
            }
            List<MissionDescription> missionDescriptions = JSONObject.parseArray(mission.getDescription(), MissionDescription.class);
            mission.setDescription1(missionDescriptions.get(0));
            mission.setDescription2(missionDescriptions.get(1));
            List<MissionReward> rewards = mission.getRewards();
            StringBuilder rewardExplain = new StringBuilder();
            for (MissionReward reward : rewards) {
                String valueByCode = UserAssetsService.getSettlementNameById(reward.getSettlementId());
                rewardExplain.append("+").append(reward.getValue()).append(valueByCode).append("   ");
            }
            mission.setRewardExplain(rewardExplain.toString());

            Integer taskCode = mission.getTaskCode();
            if (taskCode == MissionConst.SIGN_DAY) {
                // 是否已经签到
                mission.setState(!signInService.getToDayIsSign(userId));
            } else if (taskCode == MissionConst.CERTIFICATION) {
                // 是否已经实名
                mission.setState(!certificationService.isPassed(userId));
            } else if (taskCode == MissionConst.SHARE) {
                // 是否超过 每日分享 限制
                int share = NumberUtils.toInt(stringRedisTemplate.opsForValue().get(String.format(RedisKeyConst.USER_SHARE_TIME, userId)), 0);
                mission.setState(share < globalConfigService.getInt(GlobalConfigService.Enum.DAILY_SHARE_TIME));
            }
        }
        return missions;
    }

    public List<Mission> getValidList() {
        return selectListByWhereMap(ImmutableMap.of(Mission.If_valid + "=", true),
                new Pagination(1, 30),
                Mission.class, ImmutableMap.of(Mission.Sort, CommonConst.MYSQL_DESC));
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
                Mission mission = filterTaskById(missionComplete.getMissionId());
                ValueCheckUtils.notEmpty(mission, "未找到任务信息");
                if (missionComplete.getStatus() != MissionConst.MISSION_COMPLETE_STATE_FINISH) {
                    throw new BusinessException("未完成任务");
                }

                /**
                 * 1.更新个人任务状态为已领取奖励
                 * 2.按任务奖励方式增加用户资产
                 * 3.添加活动记录
                 */
                if (mission.getDisposable() || checkIfSignTask(mission.getTaskCode())) {//一次性任务 和签到任务 更新为已领取
                    MissionComplete missionComplete1 = new MissionComplete(missionCompleteId, MissionConst.MISSION_COMPLETE_STATE_RECEIVE);
                    updateIfNotNull(missionComplete1);
                } else {//非一次性任务 更新为未完成
                    MissionComplete missionComplete1 = new MissionComplete(missionCompleteId, MissionConst.MISSION_COMPLETE_STATE_NONE);
                    updateIfNotNull(missionComplete1);
                }
                /** 任务记录 任务奖金记录 */
                createAssets(userId, mission);
            }
        });
    }

    /**
     * 判断 用户类型 来 增加任务完成的资产
     */
    public BigDecimal createAssets(String userId, Mission mission) {
        BigDecimal reward = BigDecimal.ZERO;
        if (mission.getIfValid() != null && mission.getIfValid()) {
            User user = userService.getById(userId);
            List<MissionReward> missionRewards;
            if (user.getMemberType() == UserConst.USER_MEMBER_TYPE_NONE) {
                missionRewards = mission.getRewards();
            } else {
                missionRewards = mission.getMemberRewards();
            }
            for (MissionReward missionReward : missionRewards) {
                //按任务奖励方式增加用户资产
                BigDecimal rewardValue = missionReward.getValue();
                int settlementId = missionReward.getSettlementId();
                //更改资产数目
                UserAssets userAssets = userAssetsService.getAssetsByUserId(userId);
                userAssetsService.changeUserAssets(userId, rewardValue, settlementId, userAssets);
                //增加流水记录
                assetsTurnoverService.createAssetsTurnover(userId, AssetTurnoverConst.TURNOVER_TYPE_MISSION_REWARD, rewardValue,
                        AssetTurnoverConst.COMPANY_ID, userId, userAssets, settlementId, "领取" + mission.getTaskName() + "奖励");
                reward = BigDecimalUtils.add(reward, rewardValue);
            }
        }
        return reward;
    }


    /**
     * 根据会员类型获取任务奖励值
     *
     * @param memberType
     * @param mission
     * @return
     */
    public MissionReward getMissionRewardByMemberType(Integer memberType, Mission mission) {
        MissionReward missionReward;
        ValueCheckUtils.notEmpty(memberType,"会员类型不能为空");
        if(memberType == UserConst.USER_MEMBER_TYPE_COMMON){
            missionReward = mission.getMemberRewards().get(0);
        } else {
            missionReward = mission.getRewards().get(0);
        }
        BigDecimal rewardValue = missionReward.getValue();
        ValueCheckUtils.notEmpty(rewardValue,"任务会员奖励值异常");
        return missionReward;
    }


    /**
     * 完成任务并领取奖励
     *
     * @param userId       用户实体
     * @param missionCode  任务编码
     * @param turnoverDesc 流水描述
     */
    public void finishMission(String userId, Integer missionCode, String turnoverDesc) {
        Mission mission = filterTaskByCode(missionCode);
        mission.setTaskName(turnoverDesc);
        createAssets(userId, mission);
    }

    public void finishMission(String userId, Integer missionCode) {
        Mission mission = filterTaskByCode(missionCode);
        createAssets(userId, mission);
    }

    /**
     * 完成任务并领取奖励
     *
     * @param user         用户实体
     * @param missionCode  任务编码
     * @param turnoverDesc 流水描述
     */
    public BigDecimal finishMission(User user, Integer missionCode, String turnoverDesc) {
        String userId = user.getId();
        Mission mission = filterTaskByCode(missionCode);
        mission.setTaskName(turnoverDesc);
        return createAssets(userId, mission);
    }

    /**
     * 根据编码筛选任务
     *
     * @param code
     * @return
     */
    public Mission filterTaskByCode(Integer code) {
        List<Mission> missions = getValidList();
        Mission filterMission = null;
        if (missions == null) {
            throw new BusinessException("任务列表为空");
        }
        for (Mission mission : missions) {
            if (code == (mission.getTaskCode())) {
                filterMission = mission;
                break;
            }
        }
        return filterMission;
    }

    /**
     * 根据个人任务id筛选任务
     *
     * @param missionCompleteId
     * @return
     */
    public Mission filterTaskById(String missionCompleteId) {
        List<Mission> missions = getValidList();
        if (missions == null) {
            throw new BusinessException("任务列表为空");
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


    /**
     * 分享
     * 增加分享记录次数, 增加分享奖励记录
     */
    public void share(String userId) {
        String key = String.format(RedisKeyConst.USER_SHARE_TIME, userId);
        int share = NumberUtils.toInt(stringRedisTemplate.opsForValue().get(key), 0);
        if(share < globalConfigService.getInt(GlobalConfigService.Enum.DAILY_SHARE_TIME)) {
            this.finishMission(userId, MissionConst.SHARE);
            stringRedisTemplate.opsForValue().increment(key, 1);
            stringRedisTemplate.expire(key, MineDateUtils.getDaySeconds(), TimeUnit.SECONDS);
        } else {
            throw new BusinessException("超过了每天分享限制");
        }
    }


}
