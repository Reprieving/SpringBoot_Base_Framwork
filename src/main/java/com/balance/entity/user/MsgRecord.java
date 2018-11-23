package com.balance.entity.user;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Alias("MsgRecord")
@Table(name = "msg_record")//短信验证码表
public class MsgRecord implements Serializable{
    private static final long serialVersionUID = -8770953734638830657L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "user_id")
    private String userId; //用户id

    @Column(name = "phone_number")
    private String phoneNumber; //手机号码

    @Column(name = "msg_code")
    private String msgCode; //短信验证码

    @Column(name = "msg_type")
    private Integer msgType; //短信验证码类型

    @Column(name = "create_time")
    private Timestamp createTime; //

    @Column(name = "is_valid")
    private Boolean isValid; //

    public MsgRecord(String userId, String phoneNumber, String msgCode, Integer msgType, Timestamp timestamp,Boolean isValid) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.msgCode = msgCode;
        this.msgType = msgType;
        this.createTime = timestamp;
        this.isValid = isValid;
    }
}
