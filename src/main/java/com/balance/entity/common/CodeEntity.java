package com.balance.entity.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class CodeEntity<T> implements Serializable{
    private static final long serialVersionUID = -732564811154221457L;

    private String code;
    private T t;
}
