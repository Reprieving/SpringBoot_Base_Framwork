package com.balance.entity.common;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

@Data
@Alias("UserFreeCount")
@Table(name = "user_free_count")
public class UserFreeCount implements Serializable{
    private static final long serialVersionUID = -2870565182875390423L;

    @Id
    @Column(name = Id)
    private String id;

    @Column(name = User_id)
    private String user_id;

    @Column(name = Send_msg_count)
    private Integer sendMsgCount; //用户当天发送短信次数

    @Column(name = Luck_draw_count)
    private Integer luckDrawCount; //用户免费抽奖次数


    //DB Column name
    public static final String Id = "id";
    public static final String User_id = "user_id";
    public static final String Send_msg_count = "send_msg_count";
    public static final String Luck_draw_count = "luck_draw_count";
}
