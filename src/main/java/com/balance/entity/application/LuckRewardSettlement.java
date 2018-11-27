package com.balance.entity.application;

import lombok.Data;

@Data
public class LuckRewardSettlement {
    private Integer settlementId;
    private Double amount;

    public LuckRewardSettlement(Integer settlementIh, double v) {
        this.settlementId = settlementIh;
        this.amount = v;
    }
}
