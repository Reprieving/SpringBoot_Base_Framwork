package com.balance.constance;

public class AssetTurnoverConst {
    //流水类型 start
    private final static int TURNOVER_TYPE_APPLY_WITHDRAW = 1;//申请提现
    private final static int TURNOVER_TYPE_WITHDRAW_OK = 2;//提现通过
    private final static int TURNOVER_TYPE_WITHDRAW_FAIL = 3;//提现失败

    private final static int TURNOVER_TYPE_APPLY_RECHARGE = 4;//申请充值
    private final static int TURNOVER_TYPE_RECHARGE_OK = 5;//充值通过
    private final static int TURNOVER_TYPE_RECHARGE_FAIL = 6;//充值失败

    private final static int TURNOVER_TYPE_APPLY_TRANSFER = 7;//申请充值

    private final static int TURNOVER_TYPE_SHOPPING_ORDER_PAY = 9;//商城订单支付
    private final static int TURNOVER_TYPE_SHOPPING_ORDER_CANCEL = 7;//商城订单取消
    private final static int TURNOVER_TYPE_SHOPPING_GOODS_RETURN = 8;//商品退货

    private final static int TURNOVER_TYPE_MISSION_REWARD = 10;//领取任务奖励

    private final static int TURNOVER_TYPE_APPLET_REWARD = 10;//应用奖励
    //流水类型 end


    private final static int COMPANY_ID = 1;//公司流水源,流水目标id
}
