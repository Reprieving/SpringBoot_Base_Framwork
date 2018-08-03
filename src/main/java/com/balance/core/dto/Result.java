package com.balance.core.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Result<T> {
    private int stateCode;//状态码
    private String message;//信息
    private T object;//数据

}
