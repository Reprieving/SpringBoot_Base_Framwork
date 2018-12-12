package com.balance.entity.shop;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

@Data
@Alias("GoodsSku")
@Table(name = "goods_sku")//商品sku表
public class GoodsSku implements Serializable{
    private static final long serialVersionUID = 3047394843767093980L;

    @Id
    @Column(name = Id)
    private String id;

    @Column(name = Sku_no)
    private String skuNo; //sku编号

    @Column(name = Sku_name)
    private String skuName;//spu名称(冗余spu_name)

    @Column(name = Price)
    private BigDecimal price;//单价

    @Column(name = Stock)
    private Integer stock;//库存

    @Column(name = Spu_id)
    private String spuId;//spuId（goods_spud表id）

    @Column(name = Settlement_id)
    private Integer settlementId;//支付方式id

    @Column(name = Spec_json)
    private String specJson;//规格json 格式：["规格名id:规格值id","规格名id:规格值id"]

    @Column(name = Shop_id)
    private String shopId;//店铺id

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
    private List<String> imgUrl;
    //所有图片集合
    private List<GoodsImg> allImg;
    //详情图url
    private List<String> detailImgUrl;
    //详情图对象列表
    private List<GoodsImg> detailImg;
    //介绍图url
    private List<String> introduceImgUrl;
    //介绍图对象列表
    private List<GoodsImg> introduceImg;

    //DB Column name
    public static final String Id = "id";
    public static final String Sku_no = "sku_no";
    public static final String Sku_name = "sku_name";
    public static final String Price = "price";
    public static final String Stock = "stock";
    public static final String Spu_id = "spu_id";
    public static final String Settlement_id = "settlement_id";
    public static final String Spec_json = "spec_json";
    public static final String Shop_id = "shop_id";
    public static final String Create_time = "create_time";
    public static final String Update_time = "update_time";
    public static final String Status = "status";
    public static final String Is_valid = "is_valid";
}
