package com.balance.constance;

import java.math.BigDecimal;

public class ApplicationConst {
    //锁仓类型 start
    public final static int LOCKREPOSITORY_TYPE_MONTH = 1;//月锁
    public final static int LOCKREPOSITORY_TYPE_SEASON = 2;//季度锁
    public final static int LOCKREPOSITORY_TYPE_HALF_YEAR = 3;//半年锁
    public final static int LOCKREPOSITORY_TYPE_WHILE_YEAR = 4;//整年锁

    //锁仓状态 start
    public final static int LOCKREPOSITORY_STATUS_NONE = 0;//未开始
    public final static int LOCKREPOSITORY_STATUS_START = 1;//已开始
    public final static int LOCKREPOSITORY_STATUS_END = 2;//已结束
    public final static int LOCKREPOSITORY_STATUS_CANCEL = 3;//已取消

    //锁仓订单状态 start
    public final static int LOCKREPOSITORY_ORDER_STATUS_FROZEN = 0;//冻结（用于每天计算订单收益时上锁）
    public final static int LOCKREPOSITORY_ORDER_STATUS_NONE = 1;//待收益
    public final static int LOCKREPOSITORY_ORDER_STATUS_RECEIVE = 1;//已收益
    public final static int LOCKREPOSITORY_ORDER_STATUS_CANCEL = -1;//已取消

    //转盘抽奖费用
    public static final BigDecimal LUCKDRAW_IH = new BigDecimal(1); //IH
    public static final BigDecimal LUCKDRAW_ORE = new BigDecimal(10); //矿石

    //锁仓订单数排行榜长度
    public static final Integer LOCK_REPOSITORY_ORDER_RANK_LENGTH = 100;

}
