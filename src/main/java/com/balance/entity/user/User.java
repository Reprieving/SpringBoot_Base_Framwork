package com.balance.entity.user;

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
@Alias("User")
@Table(name = "user")
public class User implements Serializable{ //用户信息

    private static final long serialVersionUID = 368896404002697116L;
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "head_picture_url")
    private String headPictureUrl; //用户名

    @Column(name = "user_name")
    private String userName; //用户名

    @Column(name = "password")
    private String password; //密码

    @Column(name = "pay_password")
    private String payPassword;//支付密码

    @Column(name = "phone_number")
    private String phoneNumber;//手机号

    @Column(name = "invite_id")
    private String inviteId;//邀请人id

    @Column(name = "invite_code")
    private String inviteCode;//邀请码

    @Column(name = "create_time")
    private Timestamp createTime;//注册时间

    //UserConst.USER_STATUS_*
    @Column(name = "status")
    private Integer status;//状态
}
