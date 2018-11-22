package com.balance.controller.app.req;

import lombok.Data;

import java.util.List;

@Data
public class OrderPayReq {
    private String addressId;
    private String settlementId;
    private List<OrderSkuReq> orderSkuReqList;
}
