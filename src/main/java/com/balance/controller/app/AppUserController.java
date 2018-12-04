package com.balance.controller.app;

import com.balance.architecture.dto.Result;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.utils.JwtUtils;
import com.balance.architecture.utils.ResultUtils;
import com.balance.architecture.utils.ValueCheckUtils;
import com.balance.client.RedisClient;
import com.balance.constance.RedisKeyConst;
import com.balance.constance.UserConst;
import com.balance.controller.app.req.UserLocation;
import com.balance.entity.user.*;
import com.balance.service.common.WjSmsService;
import com.balance.service.user.CertificationService;
import com.balance.service.user.UserAssetsService;
import com.balance.service.user.UserSendService;
import com.balance.service.user.UserService;
import com.balance.utils.RandomUtil;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("app/user")
public class AppUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserSendService userSendService;

    @Autowired
    private UserAssetsService userAssetsServices;

    @Autowired
    private WjSmsService wjSmsService;

    @Autowired
    private CertificationService certificationService;

    @Autowired
    private RedisTemplate<String, Serializable> redisCacheTemplate;

    @Autowired
    private RedisClient redisClient;

    /**
     * 发送短信
     *
     * @param user
     * @return
     * @throws BusinessException
     */
    @RequestMapping("msg/send")
    public Result<?> sendMsg4register(User user) throws BusinessException {
        String msgCode = RandomUtil.randomNumber(6);
        userSendService.createMsgRecord(user.getId(), user.getPhoneNumber(), msgCode, user.getMsgType());

        String msgTypeStr = "";
        switch (user.getMsgType()) {
            case UserConst.MSG_CODE_TYPE_REGISTER:
                msgTypeStr = "[注册]";
                break;
            case UserConst.MSG_CODE_TYPE_RESET_LOGINPWD:
                msgTypeStr = "[重置登录密码]";
                break;
            case UserConst.MSG_CODE_TYPE_SETTLE_PAYPWD:
                msgTypeStr = "[设置支付密码]";
                break;
            case UserConst.MSG_CODE_TYPE_RESET_PAYPWD:
                msgTypeStr = "[重置支付密码]";
                break;
        }

        String content = "美妆连" + msgTypeStr + "验证码： " + msgCode + "，五分钟有效。【美妆连】";
        wjSmsService.sendSms(user.getPhoneNumber(), content);

        return ResultUtils.success("发送短信成功");
    }

    /**
     * 提交注册
     *
     * @param user
     * @return
     * @throws BusinessException
     */
    @RequestMapping("register")
    public Result<?> register(User user) throws BusinessException {
        userSendService.validateMsgCode(user.getId(), user.getPhoneNumber(), user.getMsgCode(), UserConst.MSG_CODE_TYPE_REGISTER);
        userService.createUser(user);
        return ResultUtils.success("注册成功");
    }

    /**
     * 用户登录
     *
     * @param user
     * @return
     * @throws BusinessException
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("login")
    public Result<?> login(User user) throws BusinessException, UnsupportedEncodingException {
        User userInfo = userService.login(user);
        userAssetsServices.updateComputePowerRank();
        return ResultUtils.success(userInfo, "登录成功");
    }

    /**
     * 修改用户昵称
     * @param request
     * @param userName
     * @return
     * @throws BusinessException
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("info/update/{userName}")
    public Result<?> updateInfo(HttpServletRequest request, @PathVariable String userName) throws BusinessException, UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        userService.updateUserName(userId,userName);
        return ResultUtils.success("修改用户昵称成功");
    }

    /**
     * 查询用户资产
     *
     * @param request
     * @return
     */
    @RequestMapping("assets")
    public Result<?> assets(HttpServletRequest request) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        UserAssets userAssets = userAssetsServices.getAssetsByUserId(userId);
        return ResultUtils.success(userAssets, "");
    }

    /**
     * 用户邀请记录
     *
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("inviteRecord")
    public Result<?> inviteRecord(HttpServletRequest request) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        User user = userService.listInviteUser(userId);
        return ResultUtils.success(user, "");
    }

    /**
     * 设置用户头像
     *
     * @param request
     * @param file
     * @return
     */
    @RequestMapping("headPic/settle")
    public Result<?> settleHeadPic(HttpServletRequest request, MultipartFile file) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        userService.updateHeadPic(userId, file);
        return ResultUtils.success("设置头像成功");
    }

    /**
     * 申请实名认证
     *
     * @param request
     * @param files
     * @return
     * @throws BusinessException
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("cert")
    public Result<?> cert(HttpServletRequest request, MultipartFile[] files) throws BusinessException, UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();

        certificationService.createCert(userId, files);
        return ResultUtils.success("申请实名认证成功");
    }

    /**
     * 重置密码
     *
     * @param request
     * @param userReq
     */
    @RequestMapping("pwd/reset")
    public Result<?> resetPassword(HttpServletRequest request, @RequestBody User userReq) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();

        userSendService.validateMsgCode(userId, userReq.getPhoneNumber(), userReq.getMsgCode(), userReq.getMsgType());
        userService.resetPassword(userId, userReq.getNewPassword(), userReq.getMsgType());

        return ResultUtils.success("重置密码成功");

    }

    /**
     * 设置支付密码
     *
     * @param request
     * @param userReq
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("payPwd/settle")
    public Result<?> settlePayPassword(HttpServletRequest request, @RequestBody User userReq) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        Integer msgType = UserConst.MSG_CODE_TYPE_SETTLE_PAYPWD;

        userSendService.validateMsgCode(userId, userReq.getPhoneNumber(), userReq.getMsgCode(), msgType);
        userService.resetPassword(userId, userReq.getNewPassword(), msgType);

        return ResultUtils.success("设置支付密码成功");
    }

    /**
     * 修改密码
     * @param request
     * @param userReq
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("pwd/update")
    public Result<?> updatePassword(HttpServletRequest request, @RequestBody User userReq) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        userService.updatePassword(userId,userReq.getPhoneNumber(),userReq.getOldPassword(),userReq.getNewPassword(),userReq.getUpdatePwdType());
        return ResultUtils.success("修改密码成功");
    }

    /**
     * 首页算力排行榜
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("computePowerRank")
    public Result<?> listComputePowerRank(HttpServletRequest request) throws UnsupportedEncodingException {
        User user = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME));
        UserComputePowerRank userComputePowerRank = new UserComputePowerRank();
        userComputePowerRank.setUserName(user.getUserName());
        userComputePowerRank.setComputeRankNo(Integer.valueOf(redisClient.get(RedisKeyConst.COMPUTE_POWER_RANK_NO + user.getId())));
        userComputePowerRank.setUserAssetsList((List<UserAssets>) redisClient.listRange(RedisKeyConst.COMPUTE_POWER_RANK_LIST,0,9));

        return ResultUtils.success(userComputePowerRank);
    }

    /**
     * 附近的人
     * @param request
     * @param userLocation
     * @return
     * @throws UnsupportedEncodingException
     */
    @ResponseBody
    @RequestMapping(value = "nearByUserList", method = RequestMethod.POST)
    public Result<?> nearByUserList(HttpServletRequest request,UserLocation userLocation) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        UserAssets userAssets = userAssetsServices.getAssetsByUserId(userId);
        ValueCheckUtils.notEmpty(userAssets,"未找到用户资产");
        return ResultUtils.success(userService.nearUserList(userId,userLocation.getProvinceCode(),
                userLocation.getCityCode(),userLocation.getRegionCode(),userLocation.getStreetCode(),
                userLocation.getCoordinateX(),userLocation.getCoordinateY(),userAssets.getComputePower()));
    }
}
