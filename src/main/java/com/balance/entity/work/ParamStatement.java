package com.balance.entity.work;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Setter
@Getter
@Alias("ParamStatement")
@Table(name = "work_param_statement")
public class ParamStatement implements Serializable {
    private static final long serialVersionUID = -2170145517447409836L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "appInterface_id")
    private String appInterfaceId;

    @Column(name = "data_key")
    private String dataKey;

    @Column(name = "data_type")
    private String dataType;

    @Column(name = "remark")
    private String remark;

    @Column(name = "req_rsp_type")
    private String reqRspType;
}
