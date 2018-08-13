package com.balance.sys.entity;

import com.balance.core.mybatis.annotation.Column;
import com.balance.core.mybatis.annotation.Id;
import com.balance.core.mybatis.annotation.Table;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Getter
@Setter
@Alias("RoleFunction")
@Table(name = "sys_role_function")
@Component
public class RoleFunction implements Serializable {
    private static final long serialVersionUID = 6683333238467964092L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "function_id")
    private String functionId;

    @Column(name = "role_id")
    private String roleId;


}
