package com.balance.entity.user;


import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.math.BigDecimal;

@Setter
@Getter
@Alias("MiningRuler")
@Table(name = "mining_ruler")
public class MiningRuler implements Serializable{ //挖矿规则
    private static final long serialVersionUID = 3915505668699553919L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "start_value")
    private BigDecimal startValue; //开始值

    @Column(name = "end_value")
    private BigDecimal endValue; //结束值

    @Column(name = "reward_rate")
    private BigDecimal rewardRate; //奖励比例

    //SettlementConst.SETTLEMENT_*
    @Column(name = "reward_type")
    private Integer rewardType; //奖励类型

}
