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
    @Column(name = Id)
    private String id;

    @Column(name = User_id)
    private String userId; //用户id

    @Column(name = Phone_number)
    private String phoneNumber; //手机号码

    @Column(name = Msg_code)
    private String msgCode; //短信验证码

    @Column(name = Msg_type)
    private Integer msgType; //短信验证码类型 UserConst.MSG_CODE_TYPE_*

    @Column(name = Create_time)
    private Timestamp createTime; //

    @Column(name = If_valid)
    private Boolean ifValid; //

    public MsgRecord(){
    }


    public MsgRecord(String userId, String phoneNumber, String msgCode, Integer msgType, Timestamp timestamp,Boolean ifValid) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.msgCode = msgCode;
        this.msgType = msgType;
        this.createTime = timestamp;
        this.ifValid = ifValid;
    }

    //DB Column name
    public static final String Id = "id";
    public static final String User_id = "user_id";
    public static final String Phone_number = "phone_number";
    public static final String Msg_code = "msg_code";
    public static final String Msg_type = "msg_type";
    public static final String Create_time = "create_time";
    public static final String If_valid = "if_valid";
}
