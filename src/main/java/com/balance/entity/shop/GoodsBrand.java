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

    @Column(name = Brand_name)
    private String brandName;

    @Column(name = Create_time)
    private Timestamp createTime;

    @Column(name = Update_time)
    private Timestamp updateTime;

    //DB Column name
    public static final String Id = "id";
    public static final String Brand_name = "brand_name";
    public static final String Create_time = "create_time";
    public static final String Update_time = "update_time";

}
