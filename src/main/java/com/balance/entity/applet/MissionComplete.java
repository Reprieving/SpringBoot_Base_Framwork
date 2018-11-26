package com.balance.entity.applet;

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
@Alias("MissionComplete")
@Table(name = "mission_complete")
public class MissionComplete implements Serializable{

    private static final long serialVersionUID = -6348587648041129654L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "mission_id")
    private String missionId; //任务id

    @Column(name = "user_id")
    private String userId; //用户id

    @Column(name = "reward")
    private BigDecimal reward; //奖励值

    //MissionConst.MISSION_COMPLETE_STATE_*
    @Column(name = "status")
    private Integer status; //领取状态

    public MissionComplete(String missionId, String userId, BigDecimal rewardValue, int status) {
        this.missionId = missionId;
        this.userId = userId;
        this.reward =rewardValue;
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
}
