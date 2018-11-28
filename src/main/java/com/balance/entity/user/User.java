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
import java.util.List;

@Data
@Alias("User")
@Table(name = "user_info")
public class User implements Serializable{ //用户信息

    private static final long serialVersionUID = 368896404002697116L;
    @Id
    @Column(name = Id)
    private String id;

    @Column(name = Head_picture_url)
    private String headPictureUrl; //用户名

    @Column(name = User_name)
    private String userName; //用户名

    @Column(name = Password)
    private String password; //密码

    @Column(name = Pay_password)
    private String payPassword;//支付密码

    @Column(name = Phone_number)
    private String phoneNumber;//手机号

    @Column(name = Invite_id)
    private String inviteId;//邀请人id

    @Column(name = Invite_code)
    private String inviteCode;//邀请码

    @Column(name = Create_time)
    private Timestamp createTime;//注册时间

    @Column(name = Status)
    private Integer status;//状态     //UserConst.USER_STATUS_*

    //扩展属性
    private String accessToken; //访问token
    private String msgCode; //短信验证码
    private Integer msgType; //
    private List<User> directUserList; //直接邀请用户记录
    private List<User> inDirectUserList; //间接邀请用户记录
    private String newPassword; //新密码


    //DB Column name
    public static final String Id = "id";
    public static final String User_id = "user_id";
    public static final String Head_picture_url = "head_picture_url";
    public static final String User_name = "user_name";
    public static final String Password = "password";
    public static final String Pay_password = "pay_password";
    public static final String Phone_number = "phone_number";
    public static final String Invite_id = "invite_id";
    public static final String Invite_code = "invite_code";
    public static final String Create_time = "create_time";
    public static final String Status = "status";

    public User(List<User> directUserList, List<User> inDirectUserList) {
        this.directUserList = directUserList;
        this.inDirectUserList = inDirectUserList;
    }

    public User() {

    }
}
