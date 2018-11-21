package com.balance.entity.shop;

import lombok.Data;

@Data
public class GoodsSkuSelectValue { //app商品详情页sku规格值实体

    //名id
    private String specNameId;

    //规格值id
    private String specValueId;

    //规格值
    private String specValue;

    public GoodsSkuSelectValue(String key, String value,String specValue) {
        this.specNameId = key;
        this.specValueId = value;
        this.specValue = specValue;

    }
}
