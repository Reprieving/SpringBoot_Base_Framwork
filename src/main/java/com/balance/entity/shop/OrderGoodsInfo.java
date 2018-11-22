package com.balance.entity.shop;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@Alias("OrderGoodsInfo")
public class OrderGoodsInfo {
    private String orderId;
    private String shopName;
    private Integer settlementId;
    private String settlementName;
    private BigDecimal orderPrice;
    private Timestamp createTime;
    private List<OrderItem> orderItemList;

}
