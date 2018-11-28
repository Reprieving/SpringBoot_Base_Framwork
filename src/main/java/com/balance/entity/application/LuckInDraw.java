package com.balance.entity.application;


import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

//中奖用户信息
@Data
@Alias("LuckInDraw")
@Table(name = "applet_luck_in_draw")
public class LuckInDraw implements Serializable {

    private static final long serialVersionUID = -3511658923353214853L;

    @Id
    @Column(name = Id)
    private String id;

    @Column(name = Phone_number)
    private String phoneNumber; //手机号

    @Column(name = Settlement_id)
    private Integer settlementId; //支付方式

    @Column(name = Reward_amount)
    private BigDecimal rewardAmount; //奖品数目

    @Column(name = Reward_str)
    private String rewardStr; //详情字符串

    @Column(name = Create_time)
    private Timestamp createTime;

    public LuckInDraw(String phoneNumber, Integer rewardSettlementId, BigDecimal rewardNumber) {
        this.phoneNumber = phoneNumber;
        this.settlementId = rewardSettlementId;
        this.rewardAmount = rewardNumber;
    }

    //DB Column name
    public static final String Id = "id";
    public static final String Phone_number = "phone_number";
    public static final String Settlement_id = "settlement_id";
    public static final String Reward_amount = "reward_amount";
    public static final String Reward_str = "reward_str";
    public static final String Create_time = "create_time";
}
