package com.balance.entity.mission;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by weihuaguo on 2018/12/27 11:55.
 */
public class MissionReward implements Serializable {

    private Integer settlementId;
    private BigDecimal value;

    public Integer getSettlementId() {
        return settlementId;
    }

    public void setSettlementId(Integer settlementId) {
        this.settlementId = settlementId;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
