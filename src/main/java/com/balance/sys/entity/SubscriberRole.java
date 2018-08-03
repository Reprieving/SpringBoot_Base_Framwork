package com.balance.sys.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SubscriberRole implements Serializable {
    private static final long serialVersionUID = -3735896857992515934L;
    private String id;
    private String subscriberId;
    private String roleId;
}
