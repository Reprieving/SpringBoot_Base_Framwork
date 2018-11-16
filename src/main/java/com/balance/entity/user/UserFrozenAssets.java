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
@Alias("UserFrozenAssets")
@Table(name = "user_frozen_assets")
public class UserFrozenAssets implements Serializable { //用户冻结资产
    private static final long serialVersionUID = -309595629871171513L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "user_id")
    private String userId; //用户名

    @Column(name = "ih")
    private BigDecimal ih; //IH

    @Column(name = "eth")
    private BigDecimal eth; //ETH

    @Column(name = "ore")
    private BigDecimal ore; //矿石

    @Column(name = "version")
    private Long version; //版本
}
