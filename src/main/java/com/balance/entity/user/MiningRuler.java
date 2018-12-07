package com.balance.entity.user;


import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Alias("MiningRuler")
@Table(name = "mining_ruler")
public class MiningRuler implements Serializable{ //挖矿规则
    private static final long serialVersionUID = 3915505668699553919L;

    @Id
    @Column(name = Id)
    private String id;

    @Column(name = Start_value)
    private BigDecimal startValue; //开始值

    @Column(name = End_value)
    private BigDecimal endValue; //结束值

    @Column(name = Reward_rate)
    private BigDecimal rewardRate; //奖励比例

    //SettlementConst.SETTLEMENT_*
    @Column(name = Reward_type)
    private Integer rewardType; //奖励类型

    //DB Column name
    public static final String Id = "id";
    public static final String Start_value = "start_value";
    public static final String End_value = "end_value";
    public static final String Reward_rate = "reward_rate";
    public static final String Reward_type = "reward_type";

    public MiningRuler(){
    }

}
