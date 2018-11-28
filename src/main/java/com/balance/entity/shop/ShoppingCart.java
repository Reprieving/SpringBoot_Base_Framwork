package com.balance.entity.shop;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

@Data
@Alias("ShoppingCart")
@Table(name = "shopping_cart")//购物车表
public class ShoppingCart {
    @Id
    @Column(name = Id)
    private String id;

    @Column(name = User_id)
    private String userId; //user id

    @Column(name = Spu_id)
    private String spuId; //spu id

    @Column(name = Sku_id)
    private String skuId; //sku id

    @Column(name = Spec_id_json)
    private String specIdJson; //规格Id字符串 格式 = [规格名id:规格值id, 规格名id:规格值id]

    @Column(name = Spec_value_str)
    private String specValueStr;//规格值字符串 格式 = " 颜色:蓝色 尺码:L "

    @Column(name = Number)
    private Integer number; //购买数量

    @Column(name = Price)
    private BigDecimal price; //sku单价

    //DB Column name
    public static final String Id = "id";
    public static final String User_id = "user_id";
    public static final String Spu_id = "spu_id";
    public static final String Sku_id = "sku_id";
    public static final String Spec_id_json = "spec_id_json";
    public static final String Spec_value_str = "spec_value_str";
    public static final String Number = "number";
    public static final String Price = "price";

    public ShoppingCart(String userId, String spuId, String skuId, String specIdJson, String specValueStr, Integer number, BigDecimal price) {
        this.userId = userId;
        this.spuId = spuId;
        this.skuId = skuId;
        this.specIdJson = specIdJson;
        this.specValueStr = specValueStr;
        this.number = number;
        this.price = price;
    }
}
