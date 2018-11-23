package com.balance.entity.common;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

@Data
@Alias("UserSendCount")
@Table(name = "user_send_count")
public class UserSendCount implements Serializable{
    private static final long serialVersionUID = -2870565182875390423L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "id")
    private String user_id;

    @Column(name = "send_msg_count")
    private String sendMsgCount; //用户当天发送短信次数
}
