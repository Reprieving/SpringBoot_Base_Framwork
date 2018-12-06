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
import java.sql.Timestamp;

@Data
@Alias("AssetsTurnover")
@Table(name = "assets_turnover")
public class AssetsTurnover implements Serializable { //资产流水
    private static final long serialVersionUID = 816842409338595448L;

    @Id
    @Column(name = Id)
    private String id;

    @Column(name = User_id)
    private String userId; //用户名

    //AssetTurnoverConst.TURNOVER_TYPE_*
    @Column(name = Turnover_type)
    private Integer turnoverType; //流水类型

    @Column(name = Turnover_amount)
    private BigDecimal turnoverAmount; //流水数目

    @Column(name = Before_amount)
    private BigDecimal beforeAmount; //流水前数目

    @Column(name = After_amount)
    private BigDecimal afterAmount; //流水前数目

    @Column(name = Source_id)
    private String sourceId; //流水源Id

    @Column(name = Target_id)
    private String targetId; //流水目标Id

    //SettlementConst.SETTLEMENT_*
    @Column(name = Settlement_id)
    private Integer settlementId; //支付方式

    @Column(name = Create_time)
    private Timestamp createTime; //创建时间

    @Column(name = Detail_str)
    private String detailStr; //详情

    //扩展属性
    private String userName;
    private String headPictureUrl;
    private String phoneNumber;

    //DB Column name
    public static final String Id = "id";
    public static final String User_id = "user_id";
    public static final String Turnover_type = "turnover_type";
    public static final String Turnover_amount = "turnover_amount";
    public static final String Before_amount = "before_amount";
    public static final String After_amount = "after_amount";
    public static final String Source_id = "source_id";
    public static final String Target_id = "target_id";
    public static final String Settlement_id = "settlement_id";
    public static final String Create_time = "create_time";
    public static final String Detail_str = "detail_str";
}
