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
    @Column(name="id")
    private String id;

    @Column(name="phone_number")
    private String phoneNumber; //手机号

    @Column(name="settlement_id")
    private Integer settlementId; //支付方式

    @Column(name="reward_amount")
    private BigDecimal rewardAmount; //奖品数目

    @Column(name="reward_str")
    private String rewardStr; //详情字符串

    @Column(name="create_time")
    private Timestamp createTime;

    public LuckInDraw(String phoneNumber, Integer rewardSettlementId, BigDecimal rewardNumber) {
        this.phoneNumber = phoneNumber;
        this.settlementId = rewardSettlementId;
        this.rewardAmount = rewardNumber;
    }
}
