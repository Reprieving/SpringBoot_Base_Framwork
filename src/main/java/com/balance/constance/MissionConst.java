package com.balance.constance;

import java.math.BigDecimal;

public class MissionConst {
    //任务领取状态 start
    public final static int MISSION_COMPLETE_STATE_NONE = 0;//未完成
    public final static int MISSION_COMPLETE_STATE_FINISH = 1;//已完成待领取
    public final static int MISSION_COMPLETE_STATE_RECEIVE = 2;//已领取
    //任务领取情况 end

    //对应关系在e_mission的task_code字段
    public final static int CODE1 = 1;
    public final static int CODE2 = 2;
    public final static int CODE3 = 3;
    public final static int CODE4 = 4;
    public final static int CODE5 = 5;
    public final static int CODE6 = 6;
    public final static int CODE7 = 7;
    public final static int CODE8 = 8;
    public final static int CODE9 = 9;
    public final static int CODE10 = 10;
    public final static int CODE11 = 11;
    public final static int CODE12 = 12;
    public final static int CODE13 = 13;
    public final static int CODE14 = 14;
    public final static int CODE15 = 15;
    public final static int CODE16 = 16;
    public final static int CODE17 = 17;


    public final static int APP_SIGN_VIEW_COUNT = 30;//App显示签到列表数目

    //购物返利额度
    public final static BigDecimal SHOPPING_CONSUME_10_ETH = BigDecimal.valueOf(10);
    public final static BigDecimal SHOPPING_CONSUME_30_ETH = BigDecimal.valueOf(10);
    public final static BigDecimal SHOPPING_CONSUME_50_ETH = BigDecimal.valueOf(10);
}
