package com.balance.service.user;

import com.alibaba.fastjson.JSONObject;
import com.balance.architecture.dto.WeiXinInfo;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.utils.JwtUtils;
import com.balance.client.RedisClient;
import com.balance.constance.RedisKeyConst;
import com.balance.constance.UserConst;
import com.balance.mapper.user.UserFreeCountMapper;
import com.balance.utils.HttpClientUtils;
import com.balance.entity.user.User;
import com.balance.service.common.GlobalConfigService;
import com.balance.utils.ValueCheckUtils;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 第三方 账号相关 (微信、微博、QQ)
 * Created by weihuaguo on 2018/12/25 14:11.
 */
@Slf4j
@Service
public class ThirdPartyService {
    private static final String APP_WX_ACCESS_TOKEN = "weixin:access_token";

    @Autowired
    private GlobalConfigService globalConfigService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserSendService userSendService;

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private UserFreeCountMapper userFreeCountMapper;

    /**
     * 微信 通过 code 获取 openId 的 url, 请格式化字符串: 1. appId, 2.appSecret, 3. code
     */
    private static final String WX_OPEN_ID_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    /** 微信获取 ACCESS_TOKE 1. appId, 2.appSecret */
//    private static final String WX_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

    /**
     * 微信 通过access_token和openId获取用户信息 的 url, 请格式化字符串: 1. access_token, 2.openid
     */
    private static final String WX_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";


//    private String getAccessToken() {
//        String accessToken = redisClient.get(APP_WX_ACCESS_TOKEN);
//        if (StringUtils.isEmpty(accessToken)) {
//            String response = HttpClientUtils.doGetSSL(String.format(WX_ACCESS_TOKEN_URL,
//                    globalConfigService.get(GlobalConfigService.Enum.APP_WEIXIN_APP_ID),
//                    globalConfigService.get(GlobalConfigService.Enum.APP_WEIXIN_APP_SECRET)));
//            if (!response.contains("access_token")) {
//                throw new BusinessException("获取微信用户信息错误");
//            }
//            WeiXinInfo weiXinInfo = JSONObject.parseObject(response, WeiXinInfo.class);
//            accessToken = weiXinInfo.getAccessToken();
//            redisClient.setForTimeMS(APP_WX_ACCESS_TOKEN, accessToken, 7100);
//        }
//        return accessToken;
//    }


    /**
     * 通过 code 获取 微信信息 包括 accessToken 和 openId
     *
     * @param code
     * @return
     */
    public WeiXinInfo getOpendId4Wx(String code) {
        String openIdRes = HttpClientUtils.doGetSSL(String.format(WX_OPEN_ID_URL,
                globalConfigService.get(GlobalConfigService.Enum.APP_WEIXIN_APP_ID),
                globalConfigService.get(GlobalConfigService.Enum.APP_WEIXIN_APP_SECRET),
                code));
        log.info("WxOpenIdRes:{}", openIdRes);
        if (!openIdRes.contains("access_token")) {
            throw new BusinessException("获取微信用户信息错误");
        }
        WeiXinInfo weiXinInfo = JSONObject.parseObject(openIdRes, WeiXinInfo.class);
        return weiXinInfo;
    }

    /**
     * 通过 accessToken 和 openId 获取微信用户信息
     *
     * @param weiXinInfo
     * @return
     */
    public void getWxInfo(WeiXinInfo weiXinInfo) {
        String WeiXinInfoRes = HttpClientUtils.doGetSSL(String.format(WX_USER_INFO_URL,
                weiXinInfo.getAccessToken(), weiXinInfo.getOpenId()));
        log.info("WeiXinInfoRes: {}", WeiXinInfoRes);
        if (!WeiXinInfoRes.contains("openid")) {
            throw new BusinessException("获取微信用户信息错误");
        }
        BeanUtils.copyProperties(JSONObject.parseObject(WeiXinInfoRes, WeiXinInfo.class), weiXinInfo);
    }

    /**
     * 绑定微信
     *
     * @param userId
     * @param code
     * @return
     */
    public String bindWx(String userId, String code) {
        // 获取openId
        WeiXinInfo weiXinInfo = getOpendId4Wx(code);
        User user = userService.getById(userId);
        String openId = weiXinInfo.getOpenId();
        String oldOpenId = user.getWxOpenId();
        if (StringUtils.isNotBlank(oldOpenId) && oldOpenId.equals(openId)) {
            throw new BusinessException("不能重复绑定同一个微信");
        }
        if (userService.selectOneByWhereString(User.Wx_open_id + " = ", openId, User.class) != null) {
            throw new BusinessException("该微信已经绑定其它账号");
        }
        // 获取微信用户 昵称等信息
        getWxInfo(weiXinInfo);
        user = new User();
        user.setId(userId);
        user.setWxOpenId(openId);
        String nickname = weiXinInfo.getNickname();
        user.setWxNickname(nickname);
        ValueCheckUtils.notZero(userService.updateIfNotNull(user), "绑定失败");
        return nickname;
    }

    /**
     * 微信登录
     *
     * @param code
     * @return
     */
    public User wxLogin(String code, String ip) {
        WeiXinInfo weiXinInfo = getOpendId4Wx(code);
        String openId = weiXinInfo.getOpenId();
        User user = userService.selectOneByWhereString(User.Wx_open_id + " = ", openId, User.class);
        Boolean isReg = false;
        if (user != null) {
            if (StringUtils.isNotBlank(user.getPhoneNumber())) {
                //已经 注册 并绑定了手机号码 返回token
                user.setAccessToken(JwtUtils.createToken(user));
                isReg = true;
            }
        } else {
            //没有注册  要求绑定手机号码
            getWxInfo(weiXinInfo);
            user = new User();
            user.setId(openId);
            weiXinInfo.setIp(ip);
            redisClient.setForTimeMIN(String.format(RedisKeyConst.USER_IP_INFO, openId), weiXinInfo, 5);
        }
        user.setIfRegister(isReg);
        return user;
    }

    /**
     * 第三方登录 绑定手机号码
     */
    public User loginBind(String msgCode, String phoneNumber, String openId, String ip) {
        WeiXinInfo weiXinInfo = (WeiXinInfo) redisClient.get((Object) (String.format(RedisKeyConst.USER_IP_INFO, openId)));
        if (weiXinInfo == null || !StringUtils.equals(ip, weiXinInfo.getIp())) {
            throw new BusinessException("账号状态异常, 请稍后重试");
        }
        userSendService.validateMsgCode(openId, phoneNumber, msgCode, UserConst.MSG_CODE_TYPE_BINGD_PHONE);
        User byPhone = userService.selectOneByWhereString(User.Phone_number + "=", phoneNumber, User.class);
        Boolean isReg = false;
        User user;
        if (byPhone != null) {
            // 该手机号码 已经创建账号
            if (StringUtils.isNotBlank(byPhone.getWxOpenId())) {
                // 但是已经绑定微信
                throw new BusinessException("该手机号码已经绑定其它账号");
            }
            user = new User();
            user.setId(byPhone.getId());
            user.setWxOpenId(weiXinInfo.getOpenId());
            user.setWxNickname(weiXinInfo.getNickname());
            userService.updateIfNotNull(user);
            isReg = true;
        } else {
            // 该手机号码 没有创建账号
            user = new User();
            user.setPhoneNumber(phoneNumber);
            user.setWxOpenId(weiXinInfo.getOpenId());
            user.setWxNickname(weiXinInfo.getNickname());
            userService.createUser(user);
        }
        user = userService.getById(user.getId());
        user.setIfRegister(isReg);
        user.setAccessToken(JwtUtils.createToken(user));
        return user;
    }

    /**
     * 绑定
     */
    public Map<String, String> binding(String type, String code, String userId) {
        switch (type) {
            case "wx":
                return ImmutableMap.of("nickname", bindWx(userId, code));
        }
        return null;
    }


    /**
     * 增加当天分享次數
     * @param userId
     */
    public void shareCountIncrease(String userId) {
        userFreeCountMapper.updateUserSendMsgCount(userId);
    }
}
