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
    @Column(name = Id)
    private String id;

    @Column(name = User_id)
    private String userId; //user id

    @Column(name = Shop_name)
    private String shoperName; //购物者名字

    @Column(name = Shoper_tel)
    private String shoperTel; //购物者手机号

    @Column(name = Shoper_city)
    private String shoperCity; //购买者省份

    @Column(name = Shoper_province)
    private String shoperProvince; //购买者省份

    @Column(name = Shoper_address)
    private String shoperAddress; //购买者详细地址

    @Column(name = Post_number)
    private String postNumber; //邮政编码

    @Column(name = Is_default)
    private Boolean isDefault; //默认标志

    @Column(name = Create_time)
    private Timestamp createTime; //

    //DB Column name
    public static final String Id = "id";
    public static final String User_id = "user_id";
    public static final String Shop_name = "shoper_name";
    public static final String Shoper_tel = "shoper_tel";
    public static final String Shoper_province = "shoper_province";
    public static final String Shoper_city = "shoper_city";
    public static final String Shoper_address = "shoper_address";
    public static final String Post_number = "post_number";
    public static final String Is_default = "is_default";
    public static final String Create_time = "create_time";

    public ShoppingAddress(String addressId, Boolean isDefault) {
        this.id = addressId;
        this.isDefault = isDefault;
    }
}
