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
    @Column(name = "id")
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "spu_id")
    private String spuId;

    @Column(name = "sku_id")
    private String skuId;

    @Column(name = "spec_json")
    private String specJson;

    @Column(name = "number")
    private Integer number;

    @Column(name = "price")
    private BigDecimal price;

    public ShoppingCart(String userId, String spuId, String skuId, String specStr, Integer number, BigDecimal price) {
        this.userId = userId;
        this.spuId = spuId;
        this.skuId = skuId;
        this.specJson = specStr;
        this.number = number;
        this.price = price;
    }
}
