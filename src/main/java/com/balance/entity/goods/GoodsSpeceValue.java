package com.balance.entity.goods;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.sql.Timestamp;

@Getter
@Setter
public class GoodsSpeceValue {
    private BigInteger id;
    private BigInteger spuId;
    private String specValue;
    private Timestamp createTime;
    private Timestamp updateTime;
}
