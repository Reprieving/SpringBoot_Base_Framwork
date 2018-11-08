package com.balance.architecture.mybatis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="mybatis")
@Getter
@Setter
public class MybatisConfig {
    public String typeAliasesPackage;
}
