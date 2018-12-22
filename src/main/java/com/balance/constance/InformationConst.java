package com.balance.constance;

import java.math.BigDecimal;

public class InformationConst {
    /** 首页 公告 */
    public final static int HOME_NOTICE = 1;
    /** 首页 轮播图 */
    public final static int HOME_BANNER = 2;
    /** 领小样 轮播图 */
    public final static int BEAUTY_BANNER = 3;
    /** 兑礼包 轮播图 */
    public final static int EXCHANGE_BANNER = 4;

    //文章审核状态
    public static final int ARTICLE_VERTIFY_STATUS_NONE = 0;//审核中
    public static final int ARTICLE_VERTIFY_STATUS_PASS = 1;//审核通过
    public static final int ARTICLE_VERTIFY_STATUS_UNPASS = -1;//审核不通过
    public static final BigDecimal ARTICLE_VERTIFY_REWARD_UNPASS = new BigDecimal(100);//审核通过奖励

}
