package com.balance.entity.shop;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.sql.Timestamp;

@Data
@Alias("ShoppingAddress")
@Table(name = "shopping_address")//购物车表
public class ShoppingAddress {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "user_id")
    private String userId; //user id

    @Column(name = "shoper_name")
    private String shoperName; //购物者名字

    @Column(name = "shoper_tel")
    private String shoperTel; //购物者手机号

    @Column(name = "shoper_province")
    private String shoperProvince; //购买者省份

    @Column(name = "shoper_address")
    private String shoperAddress; //购买者详细地址

    @Column(name = "post_number")
    private String postNumber; //邮政编码

    @Column(name = "is_default")
    private Boolean isDefault; //默认标志

    @Column(name = "create_time")
    private Timestamp createTime; //


    public ShoppingAddress(String addressId, Boolean isDefault) {
        this.id = addressId;
        this.isDefault = isDefault;
    }
}
