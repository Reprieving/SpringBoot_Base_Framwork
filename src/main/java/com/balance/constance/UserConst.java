package com.balance.constance;


import java.math.BigDecimal;

public class UserConst {
    //用户状态
    public static final int USER_STATUS_NORMAL = 1;//正常
    public static final int USER_STATUS_FROZEN = 0;//冻结

    //查询邀请记录类型
    public static final int USER_INVITE_TYPE_DIRECT = 1;//直接邀请
    public static final int USER_INVITE_TYPE_INDIRECT = 2;//间接邀请


    //用户实名认证状态
    public static final int USER_CERT_STATUS_NONE = 0;//认证中
    public static final int USER_CERT_STATUS_PASS = 1;//认证通过
    public static final int USER_CERT_STATUS_UNPASS = 2;//认证不通过

    //短信验证码类型
    public static final int MSG_CODE_TYPE_LOGINANDREGISTER = 1;//注册
    public static final int MSG_CODE_TYPE_RESET_LOGINPWD = 2;//重置登录密码
    public static final int MSG_CODE_TYPE_SETTLE_PAYPWD = 3;//设置支付密码
    public static final int MSG_CODE_TYPE_RESET_PAYPWD = 4;//重置支付密码
    public static final int MSG_CODE_TYPE_UNBIND_PHONE = 5;//解绑手机号码
    public static final int MSG_CODE_TYPE_BIND_PHONE = 6;//绑定手机号码

    //提交实名认证图片类型
    public static final String APPLY_CERT_PIC_TYPE_FRONT = "front";//注册
    public static final String APPLY_CERT_PIC_TYPE_BACK = "back";//注册
    public static final String APPLY_CERT_PIC_TYPE_HANDLER = "handler";//注册

    //修改密码类型
    public static final int UPDATE_PWD_TYPE_LOGIN = 1;//修改登录密码
    public static final int UPDATE_PWD_TYPE_PAY = 2;//修改支付密码

    //算力排行榜列表长度
    public static final int COMPUTE_POWER_RANK_LENGTH = 10;

    //收益最多被偷取几次
    public static final int MINING_REWARD_STOLEN_LIMIT_COUNT = 3;

    //偷取收益比例
    public static final BigDecimal MINING_REWARD_STOLEN_RATE = new BigDecimal(0.016);

    //会员类型
    public static final Integer USER_MEMBER_TYPE_NONE = 0;//非会员
    public static final Integer USER_MEMBER_TYPE_COMMON = 1;//普通会员
    public static final Integer USER_MEMBER_TYPE_SUPER = 2;//超级会员
}
