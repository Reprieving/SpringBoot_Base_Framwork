package com.balance.entity.shop;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;

@Data
@Alias("GoodsBrand")
@Table(name = "goods_brand")//商品品牌表
public class GoodsBrand implements Serializable{
    private static final long serialVersionUID = -6707589402324367572L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "brand_name")
    private String brandName;

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "update_time")
    private Timestamp updateTime;


}
