package com.balance.service.user;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.constance.AssetTurnoverConst;
import com.balance.constance.SettlementConst;
import com.balance.entity.user.AssetsTurnover;
import com.balance.entity.user.UserAssets;
import com.balance.mapper.user.AssetsTurnoverMapper;
import com.balance.utils.BigDecimalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
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


    /**
     * 查询用户的账单记录
     * @param userId 用户id
     * @param turnoverType 流水记录
     * @param pagination 分页参数
     * @return
     */
    public List<AssetsTurnover> listUserTurnover(String userId, Integer turnoverType, Pagination pagination) {
        List<AssetsTurnover> turnoverList =  assetsTurnoverMapper.listUserTurnover(userId,turnoverType,pagination);
        turnoverList.forEach(turnover->{
            turnover.setSettlementUnit(getSettlementNameById(turnover.getSettlementId()));
            turnover.setTypeStr(getTurnoverTypeNameByType(turnover.getTurnoverType()));
        });
        return turnoverList;
    }


    /**
     * 根据支付id获取支付描述
     * @param settlementId
     * @return
     */
    public String getSettlementNameById(Integer settlementId) {
        switch (settlementId){
            case SettlementConst.SETTLEMENT_IH:
                return "PT";

            case SettlementConst.SETTLEMENT_ETH:
                return "ETH";

            case SettlementConst.SETTLEMENT_ORE:
                return "矿石";

            case SettlementConst.SETTLEMENT_COMPUTING_POWER:
                return "颜值";

        }
        return "";
    }

    /**
     * 根据账单类型获取账单类型名
     * @param turnoverType
     * @return
     */
    public String getTurnoverTypeNameByType(Integer turnoverType){
        switch (turnoverType){
            case AssetTurnoverConst.TURNOVER_TYPE_SHOPPING_ORDER_PAY:
                return "商城订单支付";

            case AssetTurnoverConst.TURNOVER_TYPE_MISSION_REWARD:
                return "领取任务奖励";

            case AssetTurnoverConst.TURNOVER_TYPE_RECEIVE_MINING_REWARD:
                return "领取挖矿奖励";

            case AssetTurnoverConst.TURNOVER_TYPE_STEAL_MINING_REWARD:
                return "偷取挖矿奖励";

            case AssetTurnoverConst.TURNOVER_TYPE_APPLET_REWARD:
                return "应用奖励";

            case AssetTurnoverConst.TURNOVER_TYPE_BEAUTY_OBTAIN:
                return "线上小样领取";

            case AssetTurnoverConst.TURNOVER_TYPE_MEMBER_BECOME:
                return "年卡会员办理";

            case AssetTurnoverConst.TURNOVER_TYPE_COMPUTE_RETURN:
                return "颜值返现";

        }
        return "";
    }
}
