package com.balance.service.user;

import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.architecture.utils.ValueCheckUtils;
import com.balance.constance.AssetTurnoverConst;
import com.balance.constance.SettlementConst;
import com.balance.entity.user.MiningReward;
import com.balance.entity.user.UserAssets;
import com.balance.mapper.user.UserAssetsMapper;
import com.balance.utils.BigDecimalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class MiningRewardService extends BaseService {
    private static Integer miningAmount = 8; //挖矿奖励数目
    private static Double[] miningRateArr = {0.08, 0.14, 0.12, 0.16, 0.1, 0.15, 0.13, 0.12}; //挖矿奖励数目比例

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private UserAssetsMapper userAssetsMapper;

    @Autowired
    private UserAssetsService userAssetsService;

    @Autowired
    private AssetsTurnoverService assetsTurnoverService;

    @Autowired
    private MiningRulerService miningRulerService;

    /**
     * 每日收益产生
     */
    public void createMining() {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                //1.查询所有正常用户的收益
                List<UserAssets> userAssetsList = userAssetsMapper.listComputePower();
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
    public void obtainMinging(String miningRewardId, String userId) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                MiningReward miningReward = selectOneById(miningRewardId, MiningReward.class);
                ValueCheckUtils.notEmpty(miningReward, "未找到收益记录");
                if (!miningReward.getIsValid()) {
                    throw new BusinessException("收益已失效");
                }

                UserAssets userAssets = selectOneByWhereString("user_id = ", userId, UserAssets.class);
                BigDecimal rewardValue = miningReward.getRewardAmount();
                Integer rewardType = miningReward.getRewardType();


                //更改用户资产
                userAssetsService.changeUserAssets(userId, rewardValue, rewardType, userAssets);

                //添加流水记录
                assetsTurnoverService.createAssetsTurnover(userId, AssetTurnoverConst.TURNOVER_TYPE_MINING_REWARD, rewardValue, AssetTurnoverConst.COMPANY_ID, userId, userAssets, rewardType, "领取挖矿奖励收入");

            }
        });
    }

}
