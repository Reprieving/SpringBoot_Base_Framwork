package com.balance.controller.app.req;

import lombok.Data;

import java.util.List;

@Data
public class OrderSkuReq {
    private String spuId; //spuId
    private List<String> specNameValueStrList; //sku name value 组合字符串 String元素格式为 "规格名Id:规格值id"
    private Integer number;
}
