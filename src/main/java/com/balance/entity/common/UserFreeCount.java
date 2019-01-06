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
public class UserFreeCount implements Serializable {

    private static final long serialVersionUID = 1156891643026858831L;

    @Id
    @Column(name = Id)
    private String id;

    @Column(name = User_id)
    private String userId; //用户名

    @Column(name = Send_msg_count)
    private Integer sendMsgCount; //用户发送短信次数

    @Column(name = Luck_draw_count)
    private Integer LuckDrawCount; //用户免费圆盘抽奖次数

    @Column(name = Share_count)
    private Integer shareCount; //用戶分享次数

    @Column(name = Beauty_receive_count)
    private Integer beautyReceiveCount; //用户线上领取小样次数

    //DB Column name
    public static final String Id = "id";
    public static final String User_id = "user_id";
    public static final String Send_msg_count = "send_msg_count";
    public static final String Luck_draw_count = "luck_draw_count";
    public static final String Share_count = "share_count";
    public static final String Beauty_receive_count = "beauty_receive_count";

}
