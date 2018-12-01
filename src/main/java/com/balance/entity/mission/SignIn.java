package com.balance.entity.mission;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Alias("SignIn")
@Table(name = "mission_sign_in")
public class SignIn implements Serializable{

    private static final long serialVersionUID = 3401595594160716158L;

    @Id
    @Column(name = Id)
    private String id;

    @Column(name = User_id)
    private String userId;

    @Column(name = Sign_time)
    private Timestamp signTime;

    //扩展属性
    private String signId;
    private String signTimeStr;
    private Boolean hasSign = false;

    //DB Column name
    public static final String Id = "id";
    public static final String User_id = "user_id";
    public static final String Sign_time = "sign_time";
}
