package com.balance.controller.app.req;

import lombok.Data;

import java.util.List;

@Data
public class ShopOrderPayReq {
    private String addressId;
    private Integer settlementId;
    private List<ShopOrderSkuReq> orderSkuReqList;
}
