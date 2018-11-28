package com.balance.constance;

public class AssetTurnoverConst {
    //流水类型 start
    public final static int TURNOVER_TYPE_APPLY_WITHDRAW = 1;//申请提现
    public final static int TURNOVER_TYPE_WITHDRAW_OK = 2;//提现通过
    public final static int TURNOVER_TYPE_WITHDRAW_FAIL = 3;//提现失败

    public final static int TURNOVER_TYPE_APPLY_RECHARGE = 4;//申请充值
    public final static int TURNOVER_TYPE_RECHARGE_OK = 5;//充值通过
    public final static int TURNOVER_TYPE_RECHARGE_FAIL = 6;//充值失败

    public final static int TURNOVER_TYPE_APPLY_TRANSFER = 7;//申请充值

    public final static int TURNOVER_TYPE_SHOPPING_ORDER_PAY = 8;//商城订单支付

    public final static int TURNOVER_TYPE_MISSION_REWARD = 9;//领取任务奖励

    public final static int TURNOVER_TYPE_MINING_REWARD = 10;//领取挖矿奖励

    public final static int TURNOVER_TYPE_APPLET_REWARD = 11;//应用奖励
    //流水类型 end


    public final static String COMPANY_ID = "1";//公司流水源,流水目标id
}
