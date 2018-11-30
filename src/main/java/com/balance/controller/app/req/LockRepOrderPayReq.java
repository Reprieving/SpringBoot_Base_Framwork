package com.balance.controller.app.req;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LockRepOrderPayReq {
    private String lockRepId;//锁仓产品id
    private BigDecimal buyAmount; //购买额度
}
