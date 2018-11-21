package com.balance.entity.common;


import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

@Data
@Alias("GlobalConfig")
@Table(name = "global_config")
public class GlobalConfig implements Serializable{
    private static final long serialVersionUID = 7911955355142694036L;

    @Id
    @Column(name = "config_key")
    private String configKey;

    @Column(name = "config_value")
    private String configValue;

    @Column(name = "remark")
    private String remark;

}