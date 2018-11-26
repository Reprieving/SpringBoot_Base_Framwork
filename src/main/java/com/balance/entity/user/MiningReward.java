package com.balance.entity.user;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Alias("MiningReward")
@Table(name = "mining_reward") //挖矿奖励表
public class MiningReward implements Serializable{
    private static final long serialVersionUID = -4349115527185451339L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "user_id")
    private String userId; //用户id

    @Column(name = "compute_power")
    private BigDecimal computePower; //算力

    @Column(name = "reward_value")
    private BigDecimal rewardValue; //挖矿奖励值

    @Column(name = "reward_type")
    private Integer rewardType; //挖矿奖励类型

    @Column(name = "create_time")
    private Timestamp createTime; //创建时间

    @Column(name = "is_valid")
    private Boolean isValid; //是否有效

    public MiningReward(String userId, BigDecimal computePower, BigDecimal rewardValue, Integer rewardType) {
        this.userId = userId;
        this.computePower = computePower;
        this.rewardValue = rewardValue;
        this.rewardType = rewardType;
    }
}
