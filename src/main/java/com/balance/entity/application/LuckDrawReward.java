package com.balance.entity.application;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

//奖品信息
@Data
@Alias("LuckDrawReward")
@Table(name = "applet_luckdraw_reward")
public class LuckDrawReward {

    @Id
    @Column(name="id")
    private Integer id;

    @Column(name="reward_amount")
    private BigDecimal rewardAmount; //奖品数目

    @Column(name="settlement_id")
    private Integer settlementId; //支付方式

    @Column(name="reward_name")
    private String rewardName; //奖励名称

    @Column(name="ih_weight")
    private Integer ihWeight; //用ih抽奖时权重

    @Column(name="ore_weight")
    private Integer oreWeight; //用户矿石抽奖时权重

    @Column(name="is_price")
    private Boolean isPrice; //true属于奖品 false属于不中奖

    @Column(name="luck_type")
    private Integer luckType; //抽奖类型

    @Column(name="index")
    private Integer index; // 奖品索引

    @Column(name="is_valid")
    private Boolean isValid; //有效性
}
