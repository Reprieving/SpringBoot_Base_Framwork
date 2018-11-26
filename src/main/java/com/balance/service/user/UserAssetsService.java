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
public class UserAssetsService extends BaseService {

    private static Integer miningAmount = 8; //挖矿奖励数目
    private static Double[] miningRateArr = {0.08, 0.14, 0.12, 0.16, 0.1, 0.15, 0.13, 0.12}; //挖矿奖励数目比例

    @Autowired
    private UserAssetsService userAssetsService;

    @Autowired
    private AssetsTurnoverService assetsTurnoverService;

    @Autowired
    private UserAssetsMapper userAssetsMapper;

    @Autowired
    private MiningRulerService miningRulerService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    public String getSettlementNameById(Integer settlementId) {
        switch (settlementId) {
            case SettlementConst.SETTLEMENT_IH:
                return "IH";

            case SettlementConst.SETTLEMENT_ETH:
                return "ETH";

            case SettlementConst.SETTLEMENT_ORE:
                return "矿石";

            case SettlementConst.SETTLEMENT_COMPUTING_POWER:
                return "算力";

            case SettlementConst.SETTLEMENT_CANDY:
                return "糖果";
        }
        return null;
    }

    /**
     * 通过支付方式获取用户的资产
     *
     * @param userAssets   用户资产
     * @param settlementId 支付方式
     * @return
     * @throws BusinessException
     */
    public BigDecimal getAssetsBySettlementId(UserAssets userAssets, Integer settlementId) throws BusinessException {
        switch (settlementId) {
            case SettlementConst.SETTLEMENT_IH:
                return userAssets.getIh();

            case SettlementConst.SETTLEMENT_ETH:
                return userAssets.getEth();

            case SettlementConst.SETTLEMENT_ORE:
                return userAssets.getOre();

            case SettlementConst.SETTLEMENT_COMPUTING_POWER:
                return userAssets.getComputePower();
        }
        throw new BusinessException("错误的支付方式");
    }

    /**
     * 更改用户资产
     *
     * @param userId       用户id
     * @param amount       数目（正数为加，负数为减）
     * @param settlementId 支付方式
     * @return
     */
    public Integer changeUserAssets(String userId, BigDecimal amount, Integer settlementId) {
        UserAssets userAssets = selectOneByWhereString("user_id = ", userId, UserAssets.class);
        return changeUserAssets(userId, amount, settlementId, userAssets);
    }

    /**
     * 更改用户资产
     *
     * @param userId       用户id
     * @param amount       数目（正数为加，负数为减）
     * @param settlementId 支付方式
     * @param userAssets   用户资产实体
     * @return
     */
    public Integer changeUserAssets(String userId, BigDecimal amount, Integer settlementId, UserAssets userAssets) {
        String assetColumnName = null;
        switch (settlementId) {
            case SettlementConst.SETTLEMENT_IH:
                assetColumnName = "ih";
                break;

            case SettlementConst.SETTLEMENT_ETH:
                assetColumnName = "eth";
                break;

            case SettlementConst.SETTLEMENT_ORE:
                assetColumnName = "ore";
                break;

            case SettlementConst.SETTLEMENT_COMPUTING_POWER:
                assetColumnName = "compute_power";
                break;
        }

        return userAssetsMapper.updateUserAssets(userId, amount, assetColumnName, userAssets.getVersion());
    }

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
                BigDecimal rewardValue = miningReward.getRewardValue();
                Integer rewardType = miningReward.getRewardType();


                //更改用户资产
                userAssetsService.changeUserAssets(userId, rewardValue, rewardType, userAssets);

                //添加流水记录
                assetsTurnoverService.createAssetsTurnover(userId, AssetTurnoverConst.TURNOVER_TYPE_MINING_REWARD, rewardValue, AssetTurnoverConst.COMPANY_ID, userId, userAssets, rewardType, "领取挖矿奖励收入");

            }
        });
    }
}
