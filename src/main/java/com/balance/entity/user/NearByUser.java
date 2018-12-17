package com.balance.entity.user;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class NearByUser implements Serializable{
    private static final long serialVersionUID = 6884860559298350218L;

    public String userId;
    public String userName;
    public String headImgPic;
    public BigDecimal computePower;
    public Integer distance;

    public static final String User_id = "user_id";

    public NearByUser(){

    }

    public NearByUser(String userId, String userName, String headImgPic, String computePower, Integer distance) {
        this.userId = userId;
        this.userName = userName;
        this.headImgPic = headImgPic;
        this.computePower = new BigDecimal(computePower);
        this.distance = distance;
    }
}
