package com.balance.entity.user;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Setter
@Getter
@Alias("AssetsTurnover")
@Table(name = "assets_turnover")
public class AssetsTurnover implements Serializable { //资产流水
    private static final long serialVersionUID = 816842409338595448L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "user_id")
    private String userId; //用户名

    //AssetTurnoverConst.TURNOVER_TYPE_*
    @Column(name = "turnover_type")
    private Integer turnoverType; //流水类型

    @Column(name = "turnover_amount")
    private BigDecimal turnoverAmount; //流水数目

    @Column(name = "before_amount")
    private BigDecimal beforeAmount; //流水前数目

    @Column(name = "after_amount")
    private BigDecimal afterAmount; //流水前数目

    @Column(name = "source_id")
    private String sourceId; //流水源Id

    @Column(name = "target_id")
    private String targetId; //流水目标Id

    //SettlementConst.SETTLEMENT_*
    @Column(name = "settlement_id")
    private Integer settlementId; //支付方式

    @Column(name = "create_time")
    private Timestamp createTime; //创建时间

    @Column(name = "detail_str")
    private String detailStr; //详情
}
