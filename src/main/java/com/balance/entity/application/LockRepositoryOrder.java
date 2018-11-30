package com.balance.entity.application;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Alias("LockRepositoryOrder")
@Table(name = "applet_lock_repository_order")
public class LockRepositoryOrder implements Serializable {
    private static final long serialVersionUID = -216274760454139982L;

    @Id
    @Column(name = Id)
    private String id;

    @Column(name = Lock_repository_id)
    private String lockRepositoryId; //锁仓产品id

    @Column(name = User_id)
    private String userId; //用户id

    @Column(name = Order_amount)
    private BigDecimal orderAmount; //订单金额

    @Column(name = Total_income)
    private BigDecimal totalIncome; //总收益

    @Column(name = Status)
    private Integer status; //订单状态 ApplicationConst.LOCKREPOSITORY_ORDER_STATUS_*

    @Column(name = Expire_time)
    private Timestamp expireTime; //到期释放奖励时间

    @Column(name = Create_time)
    private Timestamp createTime; //创建时间

    //DB Column name
    public static final String Id = "id";
    public static final String Lock_repository_id = "lock_repository_id";
    public static final String User_id = "user_id";
    public static final String Order_amount = "order_amount";
    public static final String Total_income = "total_income";
    public static final String Status = "status";
    public static final String Expire_time = "expire_time";
    public static final String Create_time = "create_time";

    //扩展属性
    private Long period;//锁仓产品名


    public LockRepositoryOrder(String lockRepositoryId, String userId, BigDecimal buyAmount, BigDecimal totalIncome, Integer status, Timestamp expireTime) {
        this.lockRepositoryId = lockRepositoryId;
        this.userId = userId;
        this.orderAmount = buyAmount;
        this.totalIncome = totalIncome;
        this.status = status;
        this.expireTime = expireTime;
    }
}
