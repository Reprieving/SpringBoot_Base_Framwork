package com.balance.constance;

import java.math.BigDecimal;

public class MissionConst {
    //任务领取状态 start
    public final static int MISSION_COMPLETE_STATE_NONE = 0;//未完成
    public final static int MISSION_COMPLETE_STATE_FINISH = 1;//已完成待领取
    public final static int MISSION_COMPLETE_STATE_RECEIVE = 2;//已领取
    //任务领取情况 end

    //对应关系在e_mission的task_code字段
    public final static int NEW_REGISTER = 1; //新用户注册
    public final static int INVITE_USER_REGISTER = 2; //邀请用户注册
    public final static int FIRST_WITHDRAW = 3; //首次充值
    public final static int CERTIFICATION = 4; //实名认证
    public final static int BIND_WECHAT = 5; //微信绑定
    public final static int SIGN_DAY = 6;//每日签到
    public final static int SIGN_WEEK = 7;//每周签到
    public final static int SIGN_MONTH = 8;//每月签到
    public final static int JOIN_INVESTIGATION = 9; //参与问卷调查
    public final static int EXCHANGE_BEAUTY = 10;//兑换美妆
    public final static int SHOPPING_RETURN_COMPUTEPOWER = 11;//购物返算力
    public final static int SHOPPING_RETURN_IH = 12;//购物返IH
    public final static int SHARE = 13;//朋友圈分享
    public final static int RELEASE_ARTICLE = 14;//发布文章
    public final static int APPLY_YEAR_CARD = 15;//办年卡
    //对应关系在e_mission的task_code字段

    public final static int APP_SIGN_VIEW_COUNT = 30;//App显示签到列表数目

    //购物返利额度
    public final static BigDecimal SHOPPING_CONSUME_10_ETH = BigDecimal.valueOf(10);
    public final static BigDecimal SHOPPING_CONSUME_30_ETH = BigDecimal.valueOf(10);
    public final static BigDecimal SHOPPING_CONSUME_50_ETH = BigDecimal.valueOf(10);
}
