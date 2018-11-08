package com.balance.entity.goods;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.sql.Timestamp;

@Getter
@Setter
public class GoodsSkuSpecValue {
    private BigInteger id;
    private BigInteger spuId;
    private BigInteger specValueId;
    private Timestamp createTime;
    private Timestamp updateTime;

}
