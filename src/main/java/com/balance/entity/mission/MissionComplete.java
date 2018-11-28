package com.balance.entity.mission;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Alias("MissionComplete")
@Table(name = "mission_complete")
public class MissionComplete implements Serializable{

    private static final long serialVersionUID = -6348587648041129654L;

    @Id
    @Column(name = Id)
    private String id;

    @Column(name = Mission_id)
    private String missionId; //任务id

    @Column(name = User_id)
    private String userId; //用户id

    @Column(name = Reward_amount)
    private BigDecimal rewardAmount; //奖励值

    //MissionConst.MISSION_COMPLETE_STATE_*
    @Column(name = Status)
    private Integer status; //领取状态

    public MissionComplete(String missionId, String userId, BigDecimal rewardAmount, int status) {
        this.missionId = missionId;
        this.userId = userId;
        this.rewardAmount =rewardAmount;
        this.status = status;
    }

    public MissionComplete(String missionId, String userId, int status) {
        this.missionId = missionId;
        this.userId = userId;
        this.status = status;
    }

    public MissionComplete(String id, int status) {
        this.id = id;
        this.status = status;
    }

    //DB Column name
    public static final String Id = "id";
    public static final String Mission_id = "mission_id";
    public static final String User_id = "user_id";
    public static final String Reward_amount = "reward_amount";
    public static final String Status = "status";
}
