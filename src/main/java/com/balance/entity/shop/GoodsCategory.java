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
@Alias("GoodsCategory")
@Table(name = "goods_category")//商品类目表
public class GoodsCategory implements Serializable{

    private static final long serialVersionUID = -3052331644957889414L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "update_time")
    private Timestamp updateTime;


}
