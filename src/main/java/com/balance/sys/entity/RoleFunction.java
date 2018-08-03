package com.balance.sys.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RoleFunction implements Serializable {
    private static final long serialVersionUID = 6683333238467964092L;

    private String id;

    private String functionId;

    private String roleId;


}
