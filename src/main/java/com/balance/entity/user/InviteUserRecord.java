package com.balance.entity.user;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.Alias;
import org.bouncycastle.util.Times;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Alias("InviteUserRecord")
@Table(name = "invite_user_record")
public class InviteUserRecord implements Serializable {

    private static final long serialVersionUID = 2120887352497993703L;

    @Id
    @Column(name = Id)
    private String id;

    @Column(name = User_id)
    private String userId; //用户id

    @Column(name = Invite_user_id)
    private String inviteUserId; //邀请用户id

    @Column(name = Invite_type)
    private Integer inviteType; //邀请类型

    @Column(name = Reward_value)
    private BigDecimal rewardValue; //奖励值

    @Column(name = Register_time)
    private Timestamp registerTime; //注册时间

    //DB Column name
    public static final String Id = "id";
    public static final String User_id = "user_id";
    public static final String Invite_user_id = "invite_user_id";
    public static final String Invite_type = "invite_type";
    public static final String User_name = "user_name";
    public static final String Reward_value = "reward_value";
    public static final String If_cert = "if_cert";
    public static final String Register_time = "register_time";

    //扩展属性
    private String userName;//用户昵称
    private String headPicUrl;//头像图
    private Integer memberType;//头像图

    public InviteUserRecord(){}

    public InviteUserRecord(String userId, String inviteUserId, Integer inviteType, BigDecimal rewardValue, Timestamp registerTime) {
        this.userId = userId;
        this.inviteUserId = inviteUserId;
        this.inviteType = inviteType;
        this.rewardValue = rewardValue;
        this.registerTime = registerTime;
    }
}
