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
public class GoodsCollection implements Serializable{
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

    @Column(name = Low_price)
    private BigDecimal lowPrice;//最低价格

    @Column(name = Default_img_url)
    private String defaultImgUrl;//spu 默认图片

    //DB Column name
    public static final String Id = "id";
    public static final String User_id = "user_id";
    public static final String Spu_id = "spu_id";
    public static final String Goods_name = "goods_name";
    public static final String Low_price = "low_price";
    public static final String Default_img_url = "default_img_url";

    public GoodsCollection(String userId, String spuId, String goodsName, BigDecimal lowPrice, String defaultImgUrl) {
        this.userId = userId;
        this.spuId = spuId;
        this.goodsName = goodsName;
        this.lowPrice =lowPrice;
        this.defaultImgUrl = defaultImgUrl;
    }
}
