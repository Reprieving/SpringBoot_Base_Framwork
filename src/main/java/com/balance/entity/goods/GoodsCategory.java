package com.balance.entity.goods;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.sql.Timestamp;

@Setter
@Getter
public class GoodsCategory {
    private BigInteger id;
    private String categoryName;
    private Timestamp createTime;
    private Timestamp updateTime;

}
