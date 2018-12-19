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

@Data
@Alias("Department")
@Table(name = "sys_department")
public class Department implements Serializable {

    private static final long serialVersionUID = 9177358339345306837L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "parent_id")
    private String parentId;

    @Column(name = "department_name")
    private String departmentName;

    @Column(name = "charger")
    private String charger;

    @Column(name = "is_leaf")
    private String isLeaf;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "if_valid")
    private Boolean ifValid = true;

}
