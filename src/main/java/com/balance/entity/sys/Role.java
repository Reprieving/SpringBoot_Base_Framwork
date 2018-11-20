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
import java.sql.Timestamp;
import java.util.List;

@Data
@Alias("Role")
@Table(name = "sys_role")
public class Role implements Serializable{
    private static final long serialVersionUID = -1883711698685811644L;

    //database column
    //id
    @Id
    @Column(name = "id")
    private String id;

    //父id
    @Column(name = "parent_id")
    private String parentId;

    //角色名
    @Column(name = "role_name")
    private String roleName;

    //创建人
    @Column(name = "create_by")
    private String createBy;

    //创建时间
    @Column(name = "create_time")
    private Timestamp createTime;

    //是否有效
    @Column(name = "is_valid")
    private Boolean isValid = true;

    //value column
    private List<Function> functionList;

    //value column
    private List<RoleFunction> roleFunctions;
}
