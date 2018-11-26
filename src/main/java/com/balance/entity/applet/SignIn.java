package com.balance.entity.applet;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Alias("SignIn")
@Table(name = "mission_sign_in")
public class SignIn implements Serializable{

    private static final long serialVersionUID = 3401595594160716158L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_id")
    private Timestamp signTime;

    //扩展属性
    private String signId;
    private String signStr;
    private Boolean hasSign = false;
}
