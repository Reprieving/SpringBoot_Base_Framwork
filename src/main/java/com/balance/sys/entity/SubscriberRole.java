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
@Alias("SubscriberRole")
@Table(name = "sys_subscriber_role")
@Component
public class SubscriberRole implements Serializable {
    private static final long serialVersionUID = -3735896857992515934L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "subscriber_id")
    private String subscriberId;

    @Column(name = "role_id")
    private String roleId;
}
