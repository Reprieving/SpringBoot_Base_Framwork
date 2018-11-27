package com.balance.service.application;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.architecture.utils.ValueCheckUtils;
import com.balance.constance.ApplicationConst;
import com.balance.constance.AssetTurnoverConst;
import com.balance.constance.SettlementConst;
import com.balance.entity.application.LuckInDraw;
import com.balance.entity.application.LuckDrawReward;
import com.balance.entity.application.LuckDrawRewardInfo;
import com.balance.entity.application.LuckRewardSettlement;
import com.balance.entity.common.UserFreeCount;
import com.balance.entity.user.User;
import com.balance.entity.user.UserAssets;
import com.balance.mapper.user.UserFreeCountMapper;
import com.balance.service.user.AssetsTurnoverService;
import com.balance.service.user.UserAssetsService;
import com.balance.utils.BigDecimalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.*;

@Service
public class LuckDrawService extends BaseService {

    @Autowired
    private UserAssetsService userAssetsService;

    @Autowired
    private AssetsTurnoverService assetsTurnoverService;

    @Autowired
    private UserFreeCountMapper userFreeCountMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

    /**
     * 获取抽奖界面数据
     *
     * @param userId
     * @param rewardType 抽奖类型
     * @return
     */
    public LuckDrawRewardInfo getRewardInfo(Integer userId, Integer rewardType) {


        //每天剩余免费抽奖次数
        Integer freeLuckDrawNumber = selectOneByWhereString("user_id = ", userId, UserFreeCount.class).getLuckDrawCount();

        //中奖者列表
        List<LuckInDraw> luckIndrawUserList = getInLuckDraws();

        //用户资产
        UserAssets userAssets = selectOneByWhereString("user_id = ", userId, UserAssets.class);

        for (LuckInDraw luckDraw : luckIndrawUserList) {
            StringBuilder stringBuilder = new StringBuilder(luckDraw.getPhoneNumber());
            stringBuilder.replace(3, stringBuilder.length() - 4, "****");
            luckDraw.setPhoneNumber(stringBuilder.toString());
        }

        //抽奖支付方式列表
        List<LuckRewardSettlement> luckRewardSettlementList = new ArrayList<>(5);
        //IH
        LuckRewardSettlement luckRewardSettlement = new LuckRewardSettlement(SettlementConst.SETTLEMENT_IH, 1D);
        luckRewardSettlementList.add(luckRewardSettlement);

        //矿石
        luckRewardSettlement = new LuckRewardSettlement(SettlementConst.SETTLEMENT_ORE, 10);
        luckRewardSettlementList.add(luckRewardSettlement);

        //抽奖奖品列表
        List<LuckDrawReward> luckDrawRewards = selectListByWhereString("reward_type = ", rewardType, null, LuckDrawReward.class);

        LuckDrawRewardInfo rewardInfo = new LuckDrawRewardInfo(freeLuckDrawNumber, luckIndrawUserList, luckDrawRewards, luckRewardSettlementList, userAssets.getIh(), userAssets.getOre());

        return rewardInfo;

    }


    /**
     * 转盘抽奖
     *
     * @param userId       用户id
     * @param rewardType   抽奖类型
     * @param settlementId 支付方式
     * @return
     */
    public Integer turntableLuckReward(String userId, Integer rewardType, Integer settlementId) {
        final Integer[] inDrawIndex = {null};
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                //查询用户资产
                UserAssets userAssets = selectOneByWhereString("user = ", userId, UserAssets.class);

                //抽奖列表
                List<LuckDrawReward> luckDrawRewards = selectListByWhereString("reward_type = ", rewardType, null, LuckDrawReward.class);

                if (luckDrawRewards.size() == 0) {
                    throw new BusinessException("暂无抽奖奖品");
                }

                if (settlementId == null) {
                    throw new BusinessException("支付方式不能为空");
                }

                BigDecimal consumeValue = null;
                Boolean flag = true;
                if (settlementId == 0) {//免费抽奖
                    Integer freeLuckDrawNumber = selectOneByWhereString("user_id = ", userId, UserFreeCount.class).getLuckDrawCount();
                    if (freeLuckDrawNumber == 0) {
                        throw new BusinessException("免费抽奖次数已用完");
                    }
                    userFreeCountMapper.updateUserFreeLuckDrawCount(userId);
                    flag = false;
                } else if (settlementId == SettlementConst.SETTLEMENT_IH) { //IH方式抽奖
                    if (userAssets.getIh().compareTo(ApplicationConst.LUCKDRAW_IH) == -1) {
                        throw new BusinessException("IH不足");
                    }
                    consumeValue = ApplicationConst.LUCKDRAW_IH;

                } else if (settlementId == SettlementConst.SETTLEMENT_ORE) { //矿石方式抽奖
                    if (userAssets.getOre().compareTo(ApplicationConst.LUCKDRAW_ORE) == -1) {
                        throw new BusinessException("矿石不足");
                    }
                    consumeValue = ApplicationConst.LUCKDRAW_ORE;
                } else {
                    throw new BusinessException("转盘抽奖不支持IH,矿石之外的支付方式");
                }

                userAssetsService.changeUserAssets(userId, consumeValue, settlementId, userAssets);
                if (flag) {
                    assetsTurnoverService.createAssetsTurnover(userId, AssetTurnoverConst.TURNOVER_TYPE_APPLET_REWARD, BigDecimalUtils.transfer2Negative(consumeValue),userId,
                            AssetTurnoverConst.COMPANY_ID,userAssets,settlementId,"转盘抽奖支出");
                }

                final Integer[] sumWeight = {0};
                luckDrawRewards.stream().forEach(e -> sumWeight[0] += getWeightBySettlementId(e, settlementId));
                Boolean inDrawFlag = false;//中奖标志

                //获取抽奖奖品的索引
                Integer random = new Random().nextInt(sumWeight[0]);
                for (int i = 0; i < luckDrawRewards.size(); i++) {
                    int weight = getWeightBySettlementId(luckDrawRewards.get(i), settlementId);
                    if (random > weight) {
                        random -= weight;
                    } else {
                        for (LuckDrawReward luckDrawReward : luckDrawRewards) {
                            if (weight == getWeightBySettlementId(luckDrawReward, settlementId)) {
                                inDrawIndex[0] = i;
                                inDrawFlag = true;
                                break;
                            }
                        }
                    }
                    if (inDrawFlag) {
                        break;
                    }
                }

                //判断是否中奖
                LuckDrawReward luckDrawReward = luckDrawRewards.get(inDrawIndex[0]);
                if (luckDrawReward.getIsPrice()) {
                    User user = selectOneById(userId,User.class);
                    UserAssets userAssets1 = selectOneByWhereString("user_id = ",user.getId(),UserAssets.class);
                    ValueCheckUtils.notEmpty(user,"未找到用户");

                    Integer rewardSettlementId = luckDrawReward.getSettlementId();
                    BigDecimal rewardAmount = luckDrawReward.getRewardAmount();

                    //新增中奖记录
                    LuckInDraw luckInDraw = new LuckInDraw(user.getPhoneNumber(),rewardSettlementId,rewardAmount);
                    insertIfNotNull(luckInDraw);
                    assetsTurnoverService.createAssetsTurnover(userId,AssetTurnoverConst.TURNOVER_TYPE_APPLET_REWARD,rewardAmount,
                            AssetTurnoverConst.COMPANY_ID,userId,userAssets1,rewardSettlementId,"幸运转盘中奖收入");
                }
            }
        });

        return inDrawIndex[0];
    }

    /**
     * 获取中奖列表用户信息
     *
     * @return
     */
    public List<LuckInDraw> getInLuckDraws() {
        List<LuckInDraw> luckDraws = selectAll(new Pagination(), LuckInDraw.class);
        for (LuckInDraw luckDraw : luckDraws) {
            String settlementName = userAssetsService.getSettlementNameById(luckDraw.getSettlementId());
            luckDraw.setRewardStr(luckDraw.getRewardAmount() + settlementName);
        }
        return luckDraws;
    }

    /**
     * 根据支付方式获取奖品权重
     */
    public Integer getWeightBySettlementId(LuckDrawReward luckDrawReward,Integer settlementId){
        switch (settlementId){
            case SettlementConst.SETTLEMENT_IH:
                return luckDrawReward.getIhWeight();

            case SettlementConst.SETTLEMENT_ORE:
                return luckDrawReward.getOreWeight();
        }
        return luckDrawReward.getOreWeight();
    }
}
