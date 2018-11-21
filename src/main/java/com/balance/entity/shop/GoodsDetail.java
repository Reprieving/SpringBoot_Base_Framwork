package com.balance.entity.shop;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;


@Data
public class GoodsDetail {//商品详情实体

    //spu规格id
    private String spuSpecId;

    //规格名id
    private String specNameId;

    //规格名称
    private String specName;

    //规格值id
    private String specValueId;

    //规格值
    private String specValue;

    //sku Id
    private String skuId;

    //商品名称
    private String goodsName;

    //商品描述
    private String goodsDesc;

    //库存
    private Integer stock;

    //单价
    private BigDecimal price;

    //sku创建时间
    private Timestamp createTime;

    //介绍图片url
    private List<String> introduceImgList;

    //详情图片url
    private List<String> detailImgList;

    //skuList
    private List<GoodsSpec> goodsSpecList;
}
