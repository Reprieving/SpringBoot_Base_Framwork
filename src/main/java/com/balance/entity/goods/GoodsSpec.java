package com.balance.entity.goods;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.sql.Timestamp;

@Setter
@Getter
public class GoodsSpec {
    private BigInteger id;
    private String specNo;
    private String specName;
    private Timestamp createTime;
    private Timestamp updateTime;
}
