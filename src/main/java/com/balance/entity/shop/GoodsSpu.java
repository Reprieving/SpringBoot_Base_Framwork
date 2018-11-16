package com.balance.entity.shop;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

@Getter
@Setter
public class GoodsSpu {
    private BigInteger id;
    private String spuNo;
    private String goodsName;
    private BigDecimal lowPrice;
    private String categoryId;
    private String brainId;
    private Timestamp createTime;
    private Timestamp updateTime;
}
