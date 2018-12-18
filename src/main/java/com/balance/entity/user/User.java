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
    public String id;

    @Column(name = Head_picture_url)
    private String headPictureUrl; //头像图片url

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

    @Column(name = Type)
    private Integer type;//类型

    @Column(name = Level)
    private Integer level;//等级

    @Column(name = Wx_open_id)
    private String wxOpenId;//微信OpenId

    @Column(name = Sex)
    private Integer sex;//性别 1.男, 2.女

    @Column(name = Location)
    private String location;//所在地

    @Column(name = Birthday)
    private Timestamp birthday;//生日时间戳

    //扩展属性
    private String accessToken; //访问token
    private String msgCode; //短信验证码
    private Integer msgType; //UserConst.MSG_CODE_TYPE_* 短信验证码类型
    private List<User> directUserList; //直接邀请用户记录
    private List<User> inDirectUserList; //间接邀请用户记录
    private String newPassword; //新密码
    private String oldPassword; //旧密码
    private Integer updatePwdType; //UserConst.UPDATE_PWD_TYPE_* 修改密码类型
    private Integer lockRepOrderCount; //锁仓订单数量
    private Boolean isRegister;

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
    public static final String Type = "type";
    public static final String Level = "level";
    public static final String Wx_open_id = "wx_open_id";
    public static final String Sex = "sex";
    public static final String Location = "location";
    public static final String Birthday = "birthday";

    public User(List<User> directUserList, List<User> inDirectUserList) {
        this.directUserList = directUserList;
        this.inDirectUserList = inDirectUserList;
    }

    public User() {

    }
}
