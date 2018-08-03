package com.balance.core.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorInfo<T> {
    private Integer code = 500;
    private String message;

}

