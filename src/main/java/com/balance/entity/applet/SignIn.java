package com.balance.entity.applet;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Data
public class SignIn {
    private String id;
    private String signId;
    private String userId;
    private Timestamp signTime;
    private String signStr;
    private Boolean hasSign = false;
}
