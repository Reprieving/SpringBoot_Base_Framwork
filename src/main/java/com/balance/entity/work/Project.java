package com.balance.entity.work;

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
@Alias("Project")
@Table(name = "work_project")
public class Project implements Serializable{
    private static final long serialVersionUID = -6618894313915973579L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "remark")
    private String remark;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "is_valid")
    private Boolean isValid;
}
