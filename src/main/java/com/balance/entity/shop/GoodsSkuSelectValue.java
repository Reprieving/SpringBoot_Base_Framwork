package com.balance.entity.shop;

import lombok.Data;

@Data
public class GoodsSkuSelectValue { //app商品详情页sku规格值实体
    //skuId(goods_sku id)
    private String skuId;

    //规格值
    private String specValue;
}
