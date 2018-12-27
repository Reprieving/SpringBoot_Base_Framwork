package com.balance.service.user;

import com.alibaba.fastjson.JSONObject;
import com.balance.architecture.dto.WeiXinInfo;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.utils.JwtUtils;
import com.balance.client.RedisClient;
import com.balance.constance.RedisKeyConst;
import com.balance.utils.HttpClientUtils;
import com.balance.utils.ValueCheckUtils;
import com.balance.entity.user.User;
import com.balance.service.common.GlobalConfigService;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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

    @Autowired
    private GlobalConfigService globalConfigService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisClient redisClient;

    /** 微信 通过code获取access_token 的 url, 请格式化字符串: 1. appId, 2.appSecret, 3. code */
    private static final String WX_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";


    /** 微信 通过access_token和openId获取用户信息 的 url, 请格式化字符串: 1. access_token, 2.openid */
    private static final String WX_USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";

    public Map<String, String> binding(String type, String code, String userId) {
        switch (type) {
            case "wx":
                return ImmutableMap.of("nickname", bindWx(userId, code));
        }
        return null;
    }

    /**
     * 通过 code 获取 微信信息 包括 accessToken 和 openId
     * @param code
     * @return
     */
    public WeiXinInfo getAccessToken4Wx(String code) {
        String accessTokenRes = HttpClientUtils.doGetSSL(String.format(WX_ACCESS_TOKEN_URL,
                globalConfigService.get(GlobalConfigService.Enum.APP_WEIXIN_APP_ID),
                globalConfigService.get(GlobalConfigService.Enum.APP_WEIXIN_APP_SECRET),
                code));
        log.info("WxAccessTokenRes:{}", accessTokenRes);
        if (!accessTokenRes.contains("access_token")) {
            throw new BusinessException("获取微信用户信息错误");
        }
        WeiXinInfo weiXinInfo = JSONObject.parseObject(accessTokenRes, WeiXinInfo.class);
        return weiXinInfo;
    }

    /**
     * 通过 accessToken 和 openId 获取微信用户信息
     * @param weiXinInfo
     * @return
     */
    public WeiXinInfo getWxInfo(WeiXinInfo weiXinInfo) {
        String userInfoRes = HttpClientUtils.doGetSSL(String.format(WX_USERINFO_URL,
                weiXinInfo.getAccessToken(), weiXinInfo.getOpenId()));
        log.info("userInfoRes: {}", userInfoRes);
        if (!userInfoRes.contains("openid")) {
            throw new BusinessException("获取微信用户信息错误");
        }
        return JSONObject.parseObject(userInfoRes, WeiXinInfo.class);
    }

    /**
     * 绑定微信
     * @param userId
     * @param code
     * @return
     */
    public String bindWx(String userId, String code) {
        // 获取openId
        WeiXinInfo weiXinInfo = getAccessToken4Wx(code);
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
        weiXinInfo = getWxInfo(weiXinInfo);
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
     * @param code
     * @return
     */
    public User wxLogin(String code, String ip) {
        WeiXinInfo accessToken4Wx = getAccessToken4Wx(code);
        User user = userService.selectOneByWhereString(User.Wx_open_id + " = ", accessToken4Wx.getOpenId(), User.class);
        Boolean isReg = false;
        if (user != null) {
            if (StringUtils.isNotBlank(user.getPhoneNumber())) {
                //已经 注册 并绑定了手机号码
                user.setAccessToken(JwtUtils.createToken(user));
                isReg = true;
            }
        } else {
            //没有注册 创建用户 要求绑定手机号码
            accessToken4Wx = getWxInfo(accessToken4Wx);
            user = new User();
            user.setWxNickname(accessToken4Wx.getNickname());
            user.setWxOpenId(accessToken4Wx.getOpenId());
            userService.createUser(user);
            redisClient.setForTimeMIN(RedisKeyConst.USER_IP_INFO + user.getUserId(), ip, 5);
        }
        user.setIfRegister(isReg);
        return user;
    }

}
