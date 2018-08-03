package com.balance.work.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

@Setter
@Getter
@Alias("ParamStatement")
public class ParamStatement implements Serializable {
    private static final long serialVersionUID = -2170145517447409836L;
    private String id;
    private String appInterfaceId;
    private String dataKey;
    private String dataType;
    private String remark;
    private String reqRspType;
}
