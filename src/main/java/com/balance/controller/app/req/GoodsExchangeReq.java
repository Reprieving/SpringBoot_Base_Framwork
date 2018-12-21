package com.balance.controller.app.req;

import lombok.Data;

import java.io.Serializable;

@Data
public class GoodsExchangeReq implements Serializable{
    private static final long serialVersionUID = -6834744466102624474L;

    private String voucherId; //卡券Id
    private String spuId; //spuId
    private String addressId; //地址id
}
