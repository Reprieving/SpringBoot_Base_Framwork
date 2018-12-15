package com.balance.entity.shop;

import com.balance.architecture.dto.Pagination;
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
@Alias("ShopInfo")
@Table(name = "shop_info")//店铺信息表
public class ShopInfo implements Serializable{

    private static final long serialVersionUID = -3666361733455390654L;

    @Id
    @Column(name = Id)
    private String id;

    @Column(name = Subscriber_id)
    private String subscriberId;//后台用户id

    @Column(name = Shop_name)
    private String shopName;//店铺名称

    @Column(name = Create_time)
    private Timestamp createTime;

    @Column(name = Update_time)
    private Timestamp updateTime;

    @Column(name = Is_valid)
    private Boolean isValid;

    //扩展属性
    private Pagination pagination;

    //DB Column name
    public static final String Id = "id";
    public static final String Subscriber_id = "subscriber_id";
    public static final String Shop_name = "shop_name";
    public static final String Create_time = "create_time";
    public static final String Update_time = "update_time";
    public static final String Is_valid = "is_valid";

    public ShopInfo(){}
}
