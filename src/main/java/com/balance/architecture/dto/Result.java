package com.balance.architecture.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Result<T> {
    private int stateCode;//状态码
    private String message;//信息
    private T data;//数据
    private Integer count;
}
