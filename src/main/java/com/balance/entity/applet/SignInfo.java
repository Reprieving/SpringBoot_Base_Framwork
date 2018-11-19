package com.balance.entity.applet;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SignInfo {
    private Integer seriesSignCount; //连续签到次数
    private List<SignIn> signList;//最近7天签到信息
    private Boolean hasSignToday;//今天是否已签到
}
