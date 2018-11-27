package com.balance.constance;

public class UserConst {
    //用户状态
    public static final int USER_STATUS_NORMAL = 1;//正常
    public static final int USER_STATUS_FROZEN = -1;//冻结

    //查询邀请记录类型
    public static final int USER_INVITE_TYPE_DIRECT= 1;//直接邀请
    public static final int USER_INVITE_TYPE_INDIRECT= 2;//间接邀请


    //用户实名认证状态
    public static final int USER_CERT_STATUS_NONE = 0;//未认证
    public static final int USER_CERT_STATUS_PASS = 1;//认证通过
    public static final int USER_CERT_STATUS_UNPASS = 1;//认证不通过

    //短信验证码类型
    public static final int MSG_CODE_TYPE_REGISTER = 0;//注册
    public static final int MSG_CODE_TYPE_BACK_LOGINPWD = 1;//找回登录密码
    public static final int MSG_CODE_TYPE_SETTLE_PAYPWD = 3;//设置支付密码
    public static final int MSG_CODE_TYPE_BACK_PAYPWD = 4;//找回支付密码

    //提交实名认证图片类型
    public static final String APPLY_CERT_PIC_TYPE_FRONT = "front";//注册
    public static final String APPLY_CERT_PIC_TYPE_BACK = "back";//注册
    public static final String APPLY_CERT_PIC_TYPE_HANDLER = "handler";//注册
}
