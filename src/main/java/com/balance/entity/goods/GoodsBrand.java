package com.balance.entity.goods;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.sql.Timestamp;

@Getter
@Setter
public class GoodsBrand {
    private BigInteger id;
    private String brandName;
    private Timestamp createTime;
    private Timestamp updateTime;

}
