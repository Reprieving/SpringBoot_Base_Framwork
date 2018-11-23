package com.balance.entity.user;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

@Data
@Alias("MsgRecord")
@Table(name = "msg_record")//短信验证码表
public class MsgRecord implements Serializable{
    private static final long serialVersionUID = -8770953734638830657L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "msg_code")
    private String msgCode;

    @Column(name = "msg_type")
    private String msgType;
}
