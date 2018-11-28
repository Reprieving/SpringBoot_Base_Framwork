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
public class LockRepository implements Serializable {
    private static final long serialVersionUID = 299985596120419844L;

    @Id
    @Column(name = Id)
    private String id;

    @Column(name = Period)
    private Long period; //期数

    @Column(name = Start_amount)
    private BigDecimal startAmount; //起投额度

    @Column(name = Total_amount)
    private BigDecimal totalAmount; //总额度

    @Column(name = Purchased_amount)
    private BigDecimal purchasedAmount; //已购额度

    @Column(name = Surplus_amount)
    private BigDecimal surplusAmount; //剩余额度

    @Column(name =Daily_rate)
    private BigDecimal dailyRate; //日利率

    @Column(name = Product_detail)
    private String productDetail; //描述

    @Column(name = Start_time)
    private Timestamp startTime;//开始时间

    @Column(name = End_time)
    private Timestamp endTime;//结束时间

    @Column(name = Lock_type)
    private Integer lockType; //锁仓产品类型 ApplicaitonConst.LOCKREPOSITORY_TYPE_*

    @Column(name = Status)
    private Integer status; //锁仓产品状态 ApplicaitonConst.LOCKREPOSITORY_STATUS_*

    @Column(name = Create_time)
    private Timestamp createTime;//创建时间

    //DB Column name
    public static final String Id = "id";
    public static final String Period = "period";
    public static final String Start_amount = "start_amount";
    public static final String Total_amount = "total_amount";
    public static final String Purchased_amount = "purchased_amount";
    public static final String Surplus_amount = "surplus_amount";
    public static final String Daily_rate = "daily_rate";
    public static final String Product_detail = "product_detail";
    public static final String Start_time = "start_time";
    public static final String End_time = "end_time";
    public static final String Lock_type = "lock_type";
    public static final String Status = "status";
    public static final String Create_time = "create_time";

}
