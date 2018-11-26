package com.balance.entity.mission;

import lombok.Data;

import java.util.List;

@Data
public class SignInfo {
    private Integer seriesSignCount; //连续签到次数
    private List<SignIn> signList;//最近7天签到信息
    private Boolean hasSignToday;//今天是否已签到
}
