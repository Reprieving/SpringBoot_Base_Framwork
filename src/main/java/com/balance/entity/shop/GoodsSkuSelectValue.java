package com.balance.entity.shop;

import lombok.Data;

@Data
public class GoodsSkuSelectValue { //app商品详情页sku规格值实体

    //名id
    private String specNameId;

    //规格值id
    private String specValueId;

    //规格名id和规格值id 组合字符串 格式为 "specNameId:specValueId";
    private String specNameValueStr;

    //规格值
    private String specValue;

    public GoodsSkuSelectValue(String specNameId, String specValueId, String specValue) {
        this.specNameId = specNameId;
        this.specValueId = specValueId;
        this.specValue = specValue;
        this.specNameValueStr = specNameId + ":" + specValueId;
    }
}
