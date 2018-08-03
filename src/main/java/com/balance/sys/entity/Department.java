package com.balance.sys.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
public class Department implements Serializable {

    private static final long serialVersionUID = 9177358339345306837L;
    private String id;

    private String parentId;

    private String departmentName;

    private String charger;

    private String is_parent;

    private String createBy;

    private Timestamp createTime;

    private String isValid;

}
