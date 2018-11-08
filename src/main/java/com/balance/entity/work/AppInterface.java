package com.balance.entity.work;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Setter
@Getter
@Alias("AppInterface")
@Table(name = "work_api")
@Component
public class AppInterface implements Serializable {
    private static final long serialVersionUID = 4556114888253887694L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name="parent_id")
    private String parentsId;

    @Column(name="project_id")
    private String projectsId;

    @Column(name="version_no")
    private String versionNo;

    @Column(name="interface_name")
    private String interfaceName;

    @Column(name="http_type")
    private String httpType;

    @Column(name="url")
    private String url;

    private List<ParamStatement> reqParamStatement;

    @Column(name="req_param_content")
    private String reqParamContent;

    private List<ParamStatement> rspParamStatement;

    @Column(name="rsp_param_content")
    private String rspParamContent;

    @Column(name="is_project_root")
    private Boolean isProjectRoot;

    @Column(name="is_directory")
    private Boolean isDirectory;

    @Column(name="is_valid")
    private Boolean isValid;

    @Column(name="create_by")
    private String createBy;

    @Column(name="create_time")
    private Timestamp createTime;
}
