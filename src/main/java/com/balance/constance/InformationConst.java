package com.balance.constance;

import java.math.BigDecimal;

public class InformationConst {
    /** 文章类型 首页公告 */
    public final static int HOME_NOTICE = 1;
    /** 文章类型 首页轮播图 */
    public final static int HOME_BANNER = 2;
    /** 文章类型 领小样轮播图 */
    public final static int BEAUTY_BANNER = 3;
    /** 文章类型 兑礼包轮播图 */
    public final static int EXCHANGE_BANNER = 4;

    /** 内容类型 无动作 */
    public final static int CONTENT_TYPE_NULL = 1;
    /** 内容类型 链接 */
    public final static int CONTENT_TYPE_LINK = 2;
    /** 内容类型 富文本 */
    public final static int CONTENT_TYPE_HTML = 3;
    /** 内容类型 商品 */
    public final static int CONTENT_TYPE_GOODS = 4;


    //文章审核状态
    public static final int ARTICLE_VERTIFY_STATUS_NONE = 0;//审核中
    public static final int ARTICLE_VERTIFY_STATUS_PASS = 1;//审核通过
    public static final int ARTICLE_VERTIFY_STATUS_UNPASS = -1;//审核不通过
    public static final BigDecimal ARTICLE_VERTIFY_REWARD_UNPASS = new BigDecimal(100);//审核通过奖励

}
