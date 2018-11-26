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
    @Autowired
    private UserAssetsMapper userAssetsMapper;


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
}
