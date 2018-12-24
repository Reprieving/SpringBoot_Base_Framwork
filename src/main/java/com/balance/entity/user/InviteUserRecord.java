package com.balance.entity.user;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class InviteUserRecord implements Serializable {

    private String id;
    private String userId;
    private String inviteUserId;
    private String userName;
    private BigDecimal rewardValue;
    private Boolean ifCert;
}
