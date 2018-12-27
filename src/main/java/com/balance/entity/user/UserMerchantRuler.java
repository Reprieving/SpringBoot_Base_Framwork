package com.balance.entity.user;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.Alias;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Alias("UserMerchantRuler")
@Table(name = "user_merchant_ruler")
public class UserMerchantRuler implements Serializable{
    private static final long serialVersionUID = -6833487368704312664L;

    @Id
    @Column(name = Id)
    private String id;

    @Column(name = Machine_price)
    private BigDecimal machinePrice;//小样机单价

    @Column(name = Machine_rank_name)
    private String machineRankName;//商户等级名称

    @Column(name = Machine_start_count)
    private Integer machineStartCount;//小样机数量开始值

    @Column(name = Machine_end_count)
    private Integer machineEndCount;//小样机数量结束值

    @Column(name = Contract_year_times)
    private Integer contractYearTimes;//签约年数

    @Column(name = Compute_reward_rate)
    private BigDecimal computeRewardRate;//颜值奖励百分比

    @Column(name = Beauty_service_profit)
    private BigDecimal beautyServiceProfit;//线上领取小样分红

    @Column(name = Become_member_profit)
    private BigDecimal becomeMemberProfit;//办理礼盒年分红

    @Column(name = Compute_return_rate)
    private BigDecimal computeReturnRate;//线上颜值返现

    @Column(name = If_valid)
    private Boolean ifValid;//是否有效

    //扩展属性
    private Pagination pagination;




    //DB Column name
    public static final String Id = "id";
    public static final String Machine_rank_name = "machine_rank_name";
    public static final String Machine_start_count = "machine_start_count";
    public static final String Machine_end_count = "machine_end_count";
    public static final String Contract_year_times = "contract_year_times";
    public static final String Compute_reward_rate = "compute_reward_rate";
    public static final String Beauty_service_profit = "beauty_service_profit";
    public static final String Become_member_profit = "become_member_profit";
    public static final String Compute_return_rate = "compute_return_rate";
    public static final String Machine_price = "machine_price";
    public static final String If_valid = "if_valid";



}
