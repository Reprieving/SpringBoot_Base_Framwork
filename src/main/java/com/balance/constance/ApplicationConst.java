package com.balance.constance;

public class ApplicationConst {
    //锁仓类型 start
    public final static int LOCKREPOSITORY_TYPE_MONTH = 1;//月锁
    public final static int TLOCKREPOSITORY_TYPE_SEASON = 2;//季度锁
    public final static int TLOCKREPOSITORY_TYPE_HALF_YEAR = 3;//半年锁
    public final static int TLOCKREPOSITORY_TYPE_WHILE_YEAR = 4;//整年锁
    //锁仓类型 end

    //锁仓状态 start
    public final static int LOCKREPOSITORY_STATUS_NONE = 0;//未开始
    public final static int LOCKREPOSITORY_STATUS_START = 0;//已开始
    public final static int LOCKREPOSITORY_STATUS_END = 0;//已结束
    public final static int LOCKREPOSITORY_STATUS_CANCEL = 0;//已取消
    //锁仓状态 end

    //锁仓订单状态 start
    public final static int LOCKREPOSITORY_ORDER_STATUS_NONE = 1;//待收益
    public final static int LOCKREPOSITORY_ORDER_STATUS_RECEIVE = 1;//已收益
    public final static int LOCKREPOSITORY_ORDER_STATUS_CANCEL = -1;//已取消
    //锁仓订单状态 end
}
