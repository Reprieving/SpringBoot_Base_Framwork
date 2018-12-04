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
@Alias("StealMiningRecord")
@Table(name = "user_steal_mining_record")//用户偷取收益记录
public class StealMiningRecord implements Serializable{
    private static final long serialVersionUID = 2475836158568670244L;

    @Id
    @Column(name = Id)
    public String id;

    @Column(name = Stolen_user_id)
    private String stolenUserId; //被偷取用户id

    @Column(name = Steal_user_id)
    private String stealUserId; //主动偷取用户id

    @Column(name = Head_picture_url)
    private String headPictureUrl; //头像图片url

    @Column(name = User_name)
    private String userName; //主动偷取用户名

    @Column(name = Steal_amount)
    private BigDecimal stealAmount; //偷取金额

    @Column(name = Settlement_id)
    private Integer settlementId; //偷取收益类型

    @Column(name = Create_time)
    private Timestamp create_time; //创建时间

    //DB Column name
    public static final String Id = "id";
    public static final String Stolen_user_id = "stolen_user_id";
    public static final String Steal_user_id = "steal_user_id";
    public static final String User_name = "user_name";
    public static final String Head_picture_url = "head_picture_url";
    public static final String Steal_amount = "steal_amount";
    public static final String Settlement_id = "settlement_id";
    public static final String Create_time = "create_time";

    public StealMiningRecord(String stolenUserId, String stealUserId, String userName,String headPictureUrl, BigDecimal turnoverAmount, Integer settlementId) {
        this.stolenUserId = stolenUserId;
        this.stealUserId = stealUserId;
        this.userName = userName;
        this.headPictureUrl = headPictureUrl;
        this.stealAmount = turnoverAmount;
        this.settlementId = settlementId;
    }
}
