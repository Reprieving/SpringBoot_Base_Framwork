package com.balance.constance;

import java.math.BigDecimal;

public class InformationConst {
    //文章类型
    public final static int ARTICLE_TYPE_ANNOUNCE = 0;//官方公告
    public final static int ARTICLE_TYPE_ACTIVITY = 1;//活动
    public final static int ARTICLE_TYPE_COLUMN = 2;//美妆专栏
    public final static int ARTICLE_TYPE_INFORMATION = 3;//美妆资讯
    public final static int ARTICLE_TYPE_BLOCKCHAIN = 4;//区块链
    public final static int ARTICLE_TYPE_CLASSROOM = 5;//知识讲堂

    //文章审核状态
    public static final int ARTICLE_VERTIFY_STATUS_NONE = 0;//审核中
    public static final int ARTICLE_VERTIFY_STATUS_PASS = 1;//审核通过
    public static final int ARTICLE_VERTIFY_STATUS_UNPASS = -1;//审核不通过
    public static final BigDecimal ARTICLE_VERTIFY_REWARD_UNPASS = new BigDecimal(100);//审核通过奖励

}
