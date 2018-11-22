package com.balance.service.user;

import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.constance.SettlementConst;
import com.balance.entity.user.UserAssets;
import com.balance.mapper.user.UserAssetsMapper;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserAssetsService extends BaseService {

    @Autowired
    private UserAssetsMapper userAssetsMapper;

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
     * @param userId
     * @param amount
     * @param settlementId
     * @return
     */
    public Integer changeUserAssets(String userId, BigDecimal amount, Integer settlementId) {
        UserAssets userAssets = selectOneByWhereString("user_id = ", userId, UserAssets.class);
        return changeUserAssets(userId,amount,settlementId,userAssets);
    }

    /**
     * 更改用户资产
     * @param userId
     * @param amount
     * @param settlementId
     * @param userAssets
     * @return
     */
    public Integer changeUserAssets(String userId, BigDecimal amount, Integer settlementId,UserAssets userAssets) {
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

        return userAssetsMapper.updateUserAssets(userId,amount,assetColumnName,userAssets.getVersion());
    }
}
