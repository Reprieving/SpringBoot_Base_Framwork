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
@Alias("GoodsSpu")
@Table(name = "goods_spu")//商品spu表
public class ShopInfo implements Serializable{

    private static final long serialVersionUID = -3666361733455390654L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "shop_name")
    private String shopName;

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "update_time")
    private Timestamp updateTime;

}
