package com.balance.entity.shop;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

@Setter
@Getter
@Alias("GoodsSku")
@Table(name = "goods_sku")//商品sku表
public class GoodsSku implements Serializable{
    private static final long serialVersionUID = 3047394843767093980L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "sku_no")
    private String skuNo; //sku编号

    @Column(name = "sku_name")
    private String skuName;//spu名称(冗余)

    @Column(name = "price")
    private BigDecimal price;//单价

    @Column(name = "stock")
    private Integer stock;//库存

    @Column(name = "spu_id")
    private String spuId;//spuId

    @Column(name = "shop_id")
    private String shopId;//店铺id

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "update_time")
    private Timestamp updateTime;

}
