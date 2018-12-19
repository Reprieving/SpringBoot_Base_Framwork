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
    @Column(name = Id)
    private Integer id;

    @Column(name = Reward_amount)
    private BigDecimal rewardAmount; //奖品数目

    @Column(name = Settlement_id)
    private Integer settlementId; //支付方式

    @Column(name = Reward_name)
    private String rewardName; //奖励名称

    @Column(name = Ih_weight)
    private Integer ihWeight; //用ih抽奖时权重

    @Column(name = Ore_weight)
    private Integer oreWeight; //用户矿石抽奖时权重

    @Column(name = Is_price)
    private Boolean isPrice; //true属于奖品 false属于不中奖

    @Column(name = Luck_type)
    private Integer luckType; //抽奖类型

//    @Column(name = Index)
//    private Integer index; // 奖品索引

    @Column(name = If_valid)
    private Boolean ifValid; //有效性

    //DB Column name
    public static final String Id = "id";
    public static final String Reward_amount = "reward_amount";
    public static final String Settlement_id = "settlement_id";
    public static final String Reward_name = "reward_name";
    public static final String Ih_weight = "ih_weight";
    public static final String Ore_weight = "ore_weight";
    public static final String Is_price = "is_price";
    public static final String Luck_type = "luck_type";
    public static final String Index = "index";
    public static final String If_valid = "if_valid";
}
