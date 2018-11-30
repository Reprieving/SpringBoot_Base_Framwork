package com.balance.entity.shop;


import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
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
    @Column(name = Id)
    private String id;

    @Column(name = Order_no)
    private String orderNo;

    @Column(name = Settlement_id)
    private Integer settlementId;

    @Column(name = User_id)
    private String userId;

    @Column(name = Address_id)
    private String addressId;

    @Column(name = Price)
    private BigDecimal price;

    @Column(name = Status)
    private String status;

    @Column(name = Logistic_number)
    private String logisticNumber;

    @Column(name = Create_time)
    private Timestamp createTime;


    public OrderInfo(String orderNo, Integer settlementId, String userId, String addressId, BigDecimal orderTotalPrice) {
        this.orderNo = orderNo;
        this.settlementId = settlementId;
        this.userId = userId;
        this.addressId = addressId;
        this.price = orderTotalPrice;
    }

    //DB Column name
    public static final String Id = "id";
    public static final String Order_no = "order_no";
    public static final String Settlement_id = "settlement_id";
    public static final String User_id = "user_id";
    public static final String Address_id = "address_id";
    public static final String Price = "price";
    public static final String Status = "status";
    public static final String Logistic_number = "logistic_number";
    public static final String Create_time = "create_time";
}