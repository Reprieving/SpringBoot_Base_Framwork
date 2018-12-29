package com.balance.controller.app.req;

import com.balance.entity.user.User;
import lombok.Data;

import java.util.List;

@Data
public class ShopOrderPayReq {
    private String qrCodeStr;//二维码数据字符串
    private String spuId;//spuId
    private String voucherId;//卡券Id
    private Integer orderType;//订单类型
    private String addressId; //收货地址id
    private Integer settlementId; //支付方式
    private List<ShopOrderSkuReq> orderSkuReqList; //sku 信息
}
