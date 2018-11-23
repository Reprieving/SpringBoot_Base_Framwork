package com.balance.service.user;

import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.entity.user.AssetsTurnover;
import com.balance.entity.user.UserAssets;
import com.balance.utils.BigDecimalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AssetsTurnoverService extends BaseService {

    @Autowired
    private UserAssetsService userAssetsService;

    /**
     * 创建流水记录
     *
     * @param userId         用户id
     * @param turnoverType   流水类型
     * @param turnoverAmount 流水数目
     * @param sourceId       流水源id(userId)
     * @param targetId       流水目标id(userId)
     * @param userAssets     用户资产
     * @param settlementId   支付方式
     * @param detailStr      详细信息
     */
    public void createAssetsTurnover(String userId, Integer turnoverType, BigDecimal turnoverAmount, String sourceId, String targetId, UserAssets userAssets, Integer settlementId, String detailStr) throws BusinessException {
        AssetsTurnover assetsTurnover = new AssetsTurnover();
        assetsTurnover.setUserId(userId);
        assetsTurnover.setTurnoverType(turnoverType);
        assetsTurnover.setTurnoverAmount(turnoverAmount);
        assetsTurnover.setSourceId(sourceId);
        assetsTurnover.setTargetId(targetId);
        assetsTurnover.setSettlementId(settlementId);
        assetsTurnover.setDetailStr(detailStr);

        BigDecimal targetAssetsAmount = userAssetsService.getAssetsBySettlementId(userAssets, settlementId);
        assetsTurnover.setBeforeAmount(targetAssetsAmount);
        assetsTurnover.setAfterAmount(BigDecimalUtils.add(targetAssetsAmount,turnoverAmount));

        insertIfNotNull(assetsTurnover);
    }
}
