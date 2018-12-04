package com.balance.service.user;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.architecture.utils.ValueCheckUtils;
import com.balance.constance.AssetTurnoverConst;
import com.balance.constance.CommonConst;
import com.balance.constance.SettlementConst;
import com.balance.constance.UserConst;
import com.balance.entity.user.MiningReward;
import com.balance.entity.user.StealMiningRecord;
import com.balance.entity.user.User;
import com.balance.entity.user.UserAssets;
import com.balance.mapper.user.MiningRewardMapper;
import com.balance.mapper.user.UserAssetsMapper;
import com.balance.utils.BigDecimalUtils;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MiningRewardService extends BaseService {
    private static Integer miningAmount = 8; //挖矿奖励数目
    private static Double[] miningRateArr = {0.08, 0.14, 0.12, 0.16, 0.1, 0.15, 0.13, 0.12}; //挖矿奖励数目比例

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private UserAssetsMapper userAssetsMapper;

    @Autowired
    private MiningRulerService miningRulerService;

    @Autowired
    private UserAssetsService userAssetsService;

    @Autowired
    private AssetsTurnoverService assetsTurnoverService;

    @Autowired
    private MiningRewardMapper miningRewardMapper;

    /**
     * 每日收益产生
     */
    public void createMining() {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                //1.查询所有正常用户的收益
                List<UserAssets> userAssetsList = userAssetsMapper.listComputePower(UserConst.USER_STATUS_NORMAL);
                for (UserAssets userAssets : userAssetsList) {
                    String userId = userAssets.getUserId();
                    BigDecimal computePower = userAssets.getComputePower();
                    //2.获取挖矿规则对应的比例
                    BigDecimal miningRate = miningRulerService.getMiningRateByComputePower(computePower);
                    if (miningRate == null) {
                        break;
                    }
                    List<MiningReward> miningRewards = new ArrayList<>(8);
                    for (int i = 0; i < miningAmount; i++) {
                        BigDecimal rewardValue = BigDecimalUtils.multiply(computePower, miningRate);
                        MiningReward miningReward = new MiningReward(userId, computePower, BigDecimalUtils.multiply(rewardValue, new BigDecimal(miningRateArr[i])), SettlementConst.SETTLEMENT_IH);
                        miningRewards.add(miningReward);
                    }
                    insertBatch(miningRewards, false);
                }
            }
        });
    }

    /**
     * 领取挖矿收益
     *
     * @param miningRewardId 挖矿奖励Id
     * @param userId         用户id
     */
    public void obtainMiningReward(String miningRewardId, String userId) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                Map<String, Object> whereMap = ImmutableMap.of(MiningReward.Id + "=", miningRewardId, MiningReward.User_id + "=", userId);
                MiningReward miningReward = selectOneByWhereMap(whereMap, MiningReward.class);
                ValueCheckUtils.notEmpty(miningReward, "未找到收益记录");
                if (!miningReward.getIsValid()) {
                    throw new BusinessException("收益已失效");
                }

                UserAssets userAssets = userAssetsService.getAssetsByUserId(userId);
                BigDecimal rewardValue = miningReward.getRewardAmount();
                Integer rewardType = miningReward.getRewardType();

                //更改用户资产
                userAssetsService.changeUserAssets(userId, rewardValue, rewardType, userAssets);

                //添加流水记录
                assetsTurnoverService.createAssetsTurnover(userId, AssetTurnoverConst.TURNOVER_TYPE_RECEIVE_MINING_REWARD, rewardValue, AssetTurnoverConst.COMPANY_ID, userId, userAssets, rewardType, "领取挖矿奖励收入");

            }
        });
    }

    /**
     * 查询用户挖矿奖励
     *
     * @param userId
     * @return
     */
    public List<MiningReward> listMiningReward(String userId) {
        Pagination pagination = new Pagination();
        List<MiningReward> miningRewards = selectListByWhereString(MiningReward.User_id + "=", userId, pagination, MiningReward.class);
        for (MiningReward miningReward : miningRewards) {
            miningReward.setCanSteal(true);
            if (miningReward.getStolenCount() >= UserConst.MINING_REWARD_STOLEN_LIMIT_COUNT) {
                miningReward.setCanSteal(false);
            }
        }
        return miningRewards;
    }

    /**
     * 偷取收益
     *
     * @param userId
     * @param miningRewardId
     */
    public void stealIh(String userId, String miningRewardId) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                String excMessage = "偷取收益失失败";

                MiningReward miningReward = selectOneById(miningRewardId, MiningReward.class);
                ValueCheckUtils.notEmpty(miningReward, "奖励记录不存在");

                //1.检查收益被偷取次数是否到上限
                Integer i = miningReward.getStolenCount();//
                BigDecimal turnoverAmount = BigDecimalUtils.multiply(miningReward.getRewardAmount(), UserConst.MINING_REWARD_STOLEN_RATE);//流水数目
                String stealUserId = userId; //主动偷取用户Id
                User stealUser = selectOneById(userId, User.class);
                String stolenUserId = miningReward.getUserId();//被偷取用户id
                User stolenUser = selectOneById(stolenUserId, User.class);
                Integer settlementId = miningReward.getRewardType();//支付方式


                if (i <= UserConst.MINING_REWARD_STOLEN_LIMIT_COUNT) {
                    throw new BusinessException("收益被偷取次数达到上限");
                }

                //2.增加收益被偷取次数
                miningReward.setStolenCount(i + 1);
                ValueCheckUtils.isZero(miningRewardMapper.updateStolenCount(miningReward), excMessage);

                //3.增加主动偷取用户收益,增加流水记录
                UserAssets stealUserAssets = userAssetsService.getAssetsByUserId(stealUserId);
                ValueCheckUtils.isZero(userAssetsService.changeUserAssets(userId, turnoverAmount, miningReward.getRewardType(), stealUserAssets), excMessage);
                ValueCheckUtils.isZero(assetsTurnoverService.createAssetsTurnover(userId, AssetTurnoverConst.TURNOVER_TYPE_STEAL_MINING_REWARD, turnoverAmount,
                        stolenUserId, stealUserId, stealUserAssets, settlementId, "偷取用户[" + stolenUser.getUserName() + "]收益"), excMessage);


                //4.减少被偷取用户收益, 增加被偷取记录,增加流水记录order_info
                BigDecimal stolenTurnoverAmount = BigDecimalUtils.transfer2Negative(turnoverAmount);
                UserAssets stolenUserAssets = userAssetsService.getAssetsByUserId(stolenUserId);
                ValueCheckUtils.isZero(userAssetsService.changeUserAssets(userId, stolenTurnoverAmount, miningReward.getRewardType(), stolenUserAssets), excMessage);
                ValueCheckUtils.isZero(assetsTurnoverService.createAssetsTurnover(userId, AssetTurnoverConst.TURNOVER_TYPE_STEAL_MINING_REWARD, stolenTurnoverAmount,
                        stolenUserId, stealUserId, stolenUserAssets, settlementId, "被用户[" + stealUser.getUserName() + "],偷取收益"), excMessage);


                StealMiningRecord stealMiningRecord = new StealMiningRecord(stolenUserId, stealUserId, stealUser.getUserName(), stealUser.getHeadPictureUrl(), turnoverAmount, settlementId);
                ValueCheckUtils.isZero(insertIfNotNull(stealMiningRecord), excMessage);
            }
        });
    }

    /**
     * 查询最新偷币记录
     *
     * @param userId
     * @return
     */
    public List<StealMiningRecord> listStealMiningRecord(String userId) {
        Pagination pagination = new Pagination();
        Map<String, Object> orderMap = ImmutableMap.of(StealMiningRecord.Create_time, StealMiningRecord.Create_time + CommonConst.MYSQL_DESC);
        return selectListByWhereString(StealMiningRecord.Stolen_user_id + "=", userId, pagination, StealMiningRecord.class, orderMap);
    }
}
