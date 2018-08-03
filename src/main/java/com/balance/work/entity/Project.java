package com.balance.work.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.sql.Timestamp;

@Setter
@Getter
@Alias("Project")
public class Project implements Serializable{
    private static final long serialVersionUID = -6618894313915973579L;

    private String id;

    private String projectName;

    private String remark;

    private String createBy;

    private Timestamp createTime;

    private Boolean isValid;
}
