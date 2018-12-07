package com.balance.service.user;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.entity.user.AssetsTurnover;
import com.balance.entity.user.UserAssets;
import com.balance.mapper.user.AssetsTurnoverMapper;
import com.balance.utils.BigDecimalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class AssetsTurnoverService extends BaseService {

    @Autowired
    private UserAssetsService userAssetsService;

    @Autowired
    private AssetsTurnoverMapper assetsTurnoverMapper;

    /**
     * 创建流水记录
     *
     * @param userId         用户id
     * @param turnoverType   流水类型 AssetTurnoverConst.TURNOVER_TYPE_*
     * @param turnoverAmount 流水数目
     * @param sourceId       流水源id   (userId) 系统则为 0
     * @param targetId       流水目标id (userId) 系统则为 0
     * @param userAssets     用户资产
     * @param settlementId   支付方式
     * @param detailStr      详细信息
     */
    public Integer createAssetsTurnover(String userId, Integer turnoverType, BigDecimal turnoverAmount, String sourceId, String targetId, UserAssets userAssets, Integer settlementId, String detailStr) throws BusinessException {
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

        return insertIfNotNull(assetsTurnover);
    }


    public Pagination getByPage(Map<String, Object> params) {
        Pagination pagination = new Pagination();
        pagination.setTotalRecordNumber(assetsTurnoverMapper.selectCount(params));
        pagination.setObjectList(assetsTurnoverMapper.selectByPage(params));
        return pagination;
    }



}
