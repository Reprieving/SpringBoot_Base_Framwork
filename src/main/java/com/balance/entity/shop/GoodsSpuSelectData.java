package com.balance.entity.shop;

import lombok.Data;

import java.util.List;

@Data
public class GoodsSpuSelectData {
    private List<GoodsBrand> brandList;
    private List<GoodsCategory> categoryList;
    private List<ShopInfo> shopInfoList;

    public GoodsSpuSelectData(List<GoodsBrand> goodsBrands, List<GoodsCategory> goodsCategories, List<ShopInfo> shopInfos) {
        this.brandList= goodsBrands;
        this.categoryList = goodsCategories;
        this.shopInfoList = shopInfos;
    }
}
