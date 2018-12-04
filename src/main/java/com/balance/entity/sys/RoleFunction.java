package com.balance.entity.sys;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Alias("RoleFunction")
@Table(name = "sys_role_function")
public class RoleFunction implements Serializable {
    private static final long serialVersionUID = 6683333238467964092L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "function_id")
    private String functionId;

    @Column(name = "role_id")
    private String roleId;


    public RoleFunction(String functionId, String roleId) {
        this.functionId = functionId;
        this.roleId = roleId;
    }
}
