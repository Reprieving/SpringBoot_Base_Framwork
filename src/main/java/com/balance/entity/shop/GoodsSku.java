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
    @Column(name = "id")
    private String id;

    @Column(name = "sku_no")
    private String skuNo; //sku编号

    @Column(name = "sku_name")
    private String skuName;//spu名称(冗余spu_name)

    @Column(name = "price")
    private BigDecimal price;//单价

    @Column(name = "stock")
    private Integer stock;//库存

    @Column(name = "spu_id")
    private String spuId;//spuId（goods_spud表id）

    @Column(name = "settlement_id")
    private Integer settlementId;//支付方式id

    @Column(name = "spec_json")
    private String specJson;//规格名称和值组合json 格式：{"规格名":"规格值","规格名":"规格值"}

    @Column(name = "shop_id")
    private String shopId;//店铺id

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "update_time")
    private Timestamp updateTime;

    //ShopConst.GOODS_STATUS_*
    @Column(name = "status")
    private Integer status;//sku状态

    @Column(name = "is_valid")
    private Boolean isValid;//是否有效

}
