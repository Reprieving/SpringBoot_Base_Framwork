package com.balance.entity.shop;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("ShopVoucher")
@Table(name = "shop_voucher")//卡券
public class ShopVoucher {

    @Id
    @Column(name = Id)
    private String id;

    @Column(name = Voucher_name)
    private String voucherName; //卡券名字

    @Column(name = Shop_id)
    private String shopId; //店铺id

    @Column(name = Quantity)
    private Integer quantity; //数量

    @Column(name = Description)
    public String description; //描述

    @Column(name = Voucher_type)
    private Integer voucherType; //卡券类型

    //DB Column name
    public static final String Id = "id";
    public static final String Voucher_name = "voucher_name";
    public static final String Shop_id = "shop_id";
    public static final String Quantity = "quantity";
    public static final String Description = "description";
    public static final String Voucher_type = "voucher_type";
}
