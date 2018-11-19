package com.balance.entity.shop;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;

@Getter
@Setter
@Alias("GoodsSkuSpecValue")
@Table(name = "goods_sku_spec_value")//sku规格值
public class GoodsSkuSpecValue implements Serializable{
    private static final long serialVersionUID = 73563707282467904L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "sku_id")
    private String skuId;//sku id

    @Column(name = "spec_value_id")
    private String specValueId;//规格值id

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "update_time")
    private Timestamp updateTime;

}
