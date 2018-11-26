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
@Alias("LockRepository")
@Table(name = "applet_lock_repository_order")
public class LockRepositoryOrder implements Serializable{
    private static final long serialVersionUID = -216274760454139982L;

    @Id
    @Column(name="id")
    private String id;

    @Column(name="lock_repository_id")
    private String lockRepositoryId; //锁仓产品id

    @Column(name="user_id")
    private String userId; //用户id

    @Column(name="order_amount")
    private BigDecimal orderAmount; //订单金额

    @Column(name="total_income")
    private BigDecimal totalIncome; //总收益

    @Column(name="status")
    private Integer status; //订单状态 ApplicationConst.LOCKREPOSITORY_ORDER_STATUS_*

    @Column(name="expire_time")
    private Timestamp expireTime; //到期释放奖励时间

    @Column(name="create_time")
    private Timestamp createTime; //创建时间

    public LockRepositoryOrder(String lockRepositoryId, String userId, BigDecimal buyAmount, BigDecimal totalIncome, Integer status, Timestamp expireTime) {
        this.lockRepositoryId = lockRepositoryId;
        this.userId = userId;
        this.orderAmount = buyAmount;
        this.totalIncome = totalIncome;
        this.status = status;
        this.expireTime = expireTime;
    }
}
