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
@Table(name = "applet_lock_repository")
public class LockRepository implements Serializable{
    private static final long serialVersionUID = 299985596120419844L;

    @Id
    @Column(name="id")
    private String id;

    @Column(name = "period")
    private Long period; //期数

    @Column(name = "start_amount")
    private BigDecimal startAmount; //起投额度

    @Column(name = "total_amount")
    private BigDecimal totalAmount; //总额度

    @Column(name = "purchased_amount")
    private BigDecimal purchasedAmount; //已购额度

    @Column(name = "surplus_amount")
    private BigDecimal surplusAmount; //剩余额度

    @Column(name = "daily_rate")
    private BigDecimal dailyRate; //日利率

    @Column(name = "product_detail")
    private String productDetail; //描述

    @Column(name = "start_time")
    private Timestamp startTime;//开始时间

    @Column(name = "end_time")
    private Timestamp endTime;//结束时间

    @Column(name = "lock_type")
    private Integer lockType; //锁仓产品类型 ApplicaitonConst.LOCKREPOSITORY_TYPE_*

    @Column(name = "status")
    private Integer status; //锁仓产品状态 ApplicaitonConst.LOCKREPOSITORY_STATUS_*

    @Column(name = "create_time")
    private Timestamp createTime;//创建时间

}
