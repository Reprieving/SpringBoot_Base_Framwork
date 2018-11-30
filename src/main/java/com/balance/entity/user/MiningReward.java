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
    @Column(name = Id)
    private String id;

    @Column(name = User_id)
    private String userId; //用户id

    @Column(name = Compute_power)
    private BigDecimal computePower; //算力

    @Column(name = Reward_amount)
    private BigDecimal rewardAmount; //挖矿奖励值

    @Column(name = Reward_type)
    private Integer rewardType; //挖矿奖励类型

    @Column(name = Create_time)
    private Timestamp createTime; //创建时间

    @Column(name = Is_valid)
    private Boolean isValid; //是否有效

    public MiningReward(String userId, BigDecimal computePower, BigDecimal rewardValue, Integer rewardType) {
        this.userId = userId;
        this.computePower = computePower;
        this.rewardAmount = rewardValue;
        this.rewardType = rewardType;
    }

    //DB Column name
    public static final String Id = "id";
    public static final String User_id = "user_id";
    public static final String Compute_power = "compute_power";
    public static final String Reward_amount = "reward_amount";
    public static final String Reward_type = "reward_type";
    public static final String Create_time = "create_time";
    public static final String Is_valid = "is_valid";
}