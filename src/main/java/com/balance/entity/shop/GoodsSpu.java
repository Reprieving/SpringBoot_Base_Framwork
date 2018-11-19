package com.balance.entity.shop;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;
import org.springframework.cache.annotation.Cacheable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

@Getter
@Setter
@Alias("GoodsSpu")
@Table(name = "goods_spu")//商品spu表
public class GoodsSpu implements Serializable{
    private static final long serialVersionUID = -4987682283871933822L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "spu_no")
    private String spuNo;//spu编号

    @Column(name = "goods_name")
    private String goodsName;//商品名称

    @Column(name = "low_price")
    private BigDecimal lowPrice;//最低价格

    @Column(name = "category_id")
    private String categoryId;//分类id

    @Column(name = "brand_id")
    private String brandId;//品牌id

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "update_time")
    private Timestamp updateTime;
}
