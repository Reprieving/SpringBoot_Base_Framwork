package com.balance.entity.shop;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Alias("GoodsCollection")
@Table(name = "goods_collection")//商品类目表
public class GoodsCollection implements Serializable {
    private static final long serialVersionUID = 643141697745338568L;

    @Id
    @Column(name = Id)
    private String id;

    @Column(name = User_id)
    private String userId;

    @Column(name = Spu_id)
    private String spuId;

    @Column(name = Goods_name)
    private String goodsName;//商品名称

    @Column(name = Goods_description)
    private String goodsDescription;//商品描述

    @Column(name = Low_price)
    private BigDecimal lowPrice;//最低价格

    //ShopConst.SPU_TYPE_*
    @Column(name = Spu_type)
    private Integer spuType;//spu类型

    @Column(name = Default_img_url)
    private String defaultImgUrl;//spu 默认图片

    //DB扩展
    private Integer countCollection;    //收藏数
    private Integer countBuy;    //购买数

    //DB Column name
    public static final String Id = "id";
    public static final String User_id = "user_id";
    public static final String Spu_id = "spu_id";
    public static final String Goods_name = "goods_name";
    public static final String Goods_description = "goods_description";
    public static final String Low_price = "low_price";
    public static final String Default_img_url = "default_img_url";
    public static final String Spu_type = "spu_type";

    public GoodsCollection() {
    }

    public GoodsCollection(String userId, String spuId, String goodsName,String goodsDescription, BigDecimal lowPrice, String defaultImgUrl,Integer spuType) {
        this.userId = userId;
        this.spuId = spuId;
        this.goodsName = goodsName;
        this.goodsDescription = goodsDescription;
        this.lowPrice = lowPrice;
        this.defaultImgUrl = defaultImgUrl;
        this.spuType = spuType;
    }


}
