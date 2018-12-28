package com.balance.entity.shop;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@Alias("OrderGoodsInfo")
public class OrderGoodsInfo {//用户订单列表信息
    private String orderId;
    private String orderNo;
    private String shopName;
    private String logisticNumber;
    private String userName;
    private Integer settlementId;
    private Integer status;
    private String settlementName;
    private BigDecimal orderPrice;
    private BigDecimal orderFreight;
    private Timestamp createTime;
    private List<OrderItem> orderItemList;
    private Boolean ifInvestigation;
    private Boolean ifScan;
    private Boolean ifPay;
    private String addressId;
    private String shoperName;
    private String phoneNumber;
    private String detailAddress;
    private Timestamp payTime;
    private Integer orderType;
    private Boolean ifValid;

}
