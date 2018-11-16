package com.balance.entity.shop;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.sql.Timestamp;

@Setter
@Getter
public class GoodSpuSpec {
    private BigInteger id;
    private BigInteger spuId;
    private BigInteger specId;
    private Timestamp createTime;
    private Timestamp updateTime;

}
