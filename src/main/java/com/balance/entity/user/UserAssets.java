package com.balance.entity.user;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.math.BigDecimal;

@Setter
@Getter
@Alias("UserAssets")
@Table(name = "user_assets")
public class UserAssets implements Serializable{ //用户资产
    private static final long serialVersionUID = 5279191356481637406L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "user_id")
    private String userId; //用户名

    @Column(name = "compute_power")
    private BigDecimal computePower; //算力

    @Column(name = "ih")
    private BigDecimal ih; //IH

    @Column(name = "eth")
    private BigDecimal eth; //ETH

    @Column(name = "ore")
    private BigDecimal ore; //矿石

    @Column(name = "version")
    private Long version; //版本
}
