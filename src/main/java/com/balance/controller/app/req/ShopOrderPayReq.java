package com.balance.controller.app.req;

import lombok.Data;

import java.util.List;

@Data
public class ShopOrderPayReq {
    private String addressId;
    private String settlementId;
    private List<ShopOrderSkuReq> orderSkuReqList;
}
