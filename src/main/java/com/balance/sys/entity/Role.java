package com.balance.sys.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
public class Role implements Serializable{
    private static final long serialVersionUID = -1883711698685811644L;

    //database column
    //id
    private String id;
    //父id
    private String parentId;
    //角色名
    private String roleId;
    //创建人
    private String createBy;
    //创建时间
    private Timestamp createTime;
    //是否有效
    private String isValid;

    //value column
    //接口
    private List<Function> functionList;
}
