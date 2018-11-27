package com.balance.entity.application;


import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class LuckDrawRewardInfo {
    private Integer freeDrawNumber; //每天剩余免费抽奖次数
    private List<LuckInDraw> luckIndrawUserList; //中奖用户信息
    private List<LuckDrawReward> rewardList; //奖品列表
    private List<LuckRewardSettlement> settlementList; //抽奖支付方式和支付金额
    private BigDecimal ih; //用户剩余ih
    private BigDecimal ore; //用户剩余矿石

    public LuckDrawRewardInfo(Integer freeLuckDrawNumber, List<LuckInDraw> luckIndrawUserList, List<LuckDrawReward> luckDrawRewards, List<LuckRewardSettlement> luckRewardSettlementList, BigDecimal ih, BigDecimal ore) {
        this.freeDrawNumber = freeLuckDrawNumber;
        this.luckIndrawUserList = luckIndrawUserList;
        this.rewardList = luckDrawRewards;
        this.settlementList = luckRewardSettlementList;
        this.ih = ih;
        this.ore = ore;
    }
}
