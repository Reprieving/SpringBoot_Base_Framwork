package com.balance.entity.goods;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

@Setter
@Getter
public class GoodsSku {
    private BigInteger id;
    private String skuNo;
    private String skuName;
    private BigDecimal price;
    private BigInteger stock;
    private BigInteger spuId;
    private BigInteger shopId;
    private Timestamp createTime;
    private Timestamp updateTime;

}
