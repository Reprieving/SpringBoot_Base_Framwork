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

@Data
@Alias("SubscriberRole")
@Table(name = "sys_subscriber_role")
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
