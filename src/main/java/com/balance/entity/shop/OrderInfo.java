package com.balance.entity.shop;


import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import com.sun.xml.internal.bind.v2.model.core.ID;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Alias("OrderInfo")
@Table(name = "order_info")//订单基本信息表
public class OrderInfo implements Serializable {

    private static final long serialVersionUID = 5290596013449535746L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "settlement_id")
    private Integer settlementId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "address_id")
    private String addressId;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "status")
    private String status;

    @Column(name = "logistic_number")
    private String logisticNumber;

    @Column(name = "create_time")
    private Timestamp createTime;

}
