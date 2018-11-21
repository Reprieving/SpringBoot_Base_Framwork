package com.balance.entity.shop;

import java.util.List;

public class GoodsSpec { //商品specName对应specValue实体
    private String specName;
    private List<GoodsSkuSelectValue> specValueList;

    public GoodsSpec(String specName, List<GoodsSkuSelectValue> specValueList) {
        this.specName = specName;
        this.specValueList = specValueList;
    }
}
