package com.balance.entity.shop;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;
import org.springframework.cache.annotation.Cacheable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

@Data
@Alias("GoodsSpu")
@Table(name = "goods_spu")//商品spu表
public class GoodsSpu implements Serializable{
    private static final long serialVersionUID = -4987682283871933822L;

    @Id
    @Column(name = Id)
    private String id;

    @Column(name = Spu_no)
    private String spuNo;//spu编号

    @Column(name = Goods_name)
    private String goodsName;//商品名称

    @Column(name = Low_price)
    private BigDecimal lowPrice;//最低价格

    @Column(name = Settlement_id)
    private Integer settlementId;//支付方式id

    @Column(name = Category_id)
    private String categoryId;//分类id（goods_category表id）

    @Column(name = Brand_id)
    private String brandId;//品牌id（goods_brand表id）

    @Column(name = Default_img_url)
    private String defaultImgUrl;//spu 默认图片

    @Column(name = Create_time)
    private Timestamp createTime;

    @Column(name = Update_time)
    private Timestamp updateTime;

    //ShopConst.GOODS_STATUS_*
    @Column(name = Status)
    private Integer status;//sku状态

    @Column(name = Is_valid)
    private Boolean isValid;//是否有效

    //扩展属性
    private String categoryName;//类目名
    private String brandName;//品牌名


    //DB Column name
    public static final String Id = "id";
    public static final String Spu_no = "spu_no";
    public static final String Goods_name = "goods_name";
    public static final String Low_price = "low_price";
    public static final String Settlement_id = "settlement_id";
    public static final String Category_id = "category_id";
    public static final String Brand_id = "brand_id";
    public static final String Default_img_url = "default_img_url";
    public static final String Create_time = "create_time";
    public static final String Update_time = "update_time";
    public static final String Status = "status";
    public static final String Is_valid = "is_valid";
}
