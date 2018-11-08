package com.balance.entity.goods;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.sql.Timestamp;

@Getter
@Setter
public class ShopInfo {
    private BigInteger id;
    private String shopName;
    private Timestamp createTime;
    private Timestamp updateTime;
}
