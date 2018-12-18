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
import com.balance.entity.shop.SampleMachineLocation;
import com.balance.entity.user.*;
import com.balance.service.common.WjSmsService;
import com.balance.service.shop.SampleMachineService;
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
    private CertificationService certificationService;

    @Autowired
    private SampleMachineService sampleMachineService;

    @Autowired
    private RedisClient redisClient;

    /**
     * 发送短信
     *
     * @param userReq
     * @return
     * @throws BusinessException
     */
    @RequestMapping("msg/send")
    public Result<?> sendMsg4register(HttpServletRequest request, User userReq) throws BusinessException, UnsupportedEncodingException {
        String msgCode = userSendService.createMsgRecord(request,userReq);
        return ResultUtils.success("发送短信成功："+msgCode);

//        return ResultUtils.success("发送短信成功");
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
        userSendService.validateMsgCode(user.getId(), user.getPhoneNumber(), user.getMsgCode(), UserConst.MSG_CODE_TYPE_LOGINANDREGISTER);
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
        return ResultUtils.success(userInfo, "登录成功");
    }

    /**
     * 修改用户昵称
     *
     * @param request
     * @return
     * @throws BusinessException
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("info/update")
    public Result<?> updateInfo(HttpServletRequest request, User user) throws BusinessException, UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        userService.updateUserName(userId, user.getUserName());
        return ResultUtils.success("修改用户昵称成功");
    }

    /**
     * 查询用户信息
     *
     * @param request
     * @return
     */
    @RequestMapping("info")
    public Result<?> assets(HttpServletRequest request) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        User userInfo = userService.allUserInfo(userId);
        return ResultUtils.success(userInfo);
    }

    /**
     * 邀请码设置
     *
     * @param request
     * @return
     */
    @RequestMapping("inviteCode/settle/{inviteCode}")
    public Result<?> settleInviteCode(HttpServletRequest request,@PathVariable String inviteCode) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        userService.updateInviteCode(userId,inviteCode);
        return ResultUtils.success();
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
        Object o = userService.updateHeadPic(userId, file);
        return ResultUtils.success(o,"设置头像成功");
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
     * @param userReq
     */
    @RequestMapping("pwd/reset")
    public Result<?> resetPassword(HttpServletRequest request, User userReq) throws UnsupportedEncodingException {
        String userId;
        Integer msgType = userReq.getMsgType();
        if (UserConst.MSG_CODE_TYPE_RESET_LOGINPWD == msgType) {
            User user = userService.selectOneByWhereString(User.Phone_number + "=", userReq.getPhoneNumber(), User.class);
            ValueCheckUtils.notEmpty(user, "该手机号未注册");
            userId = user.getId();
        } else if (UserConst.MSG_CODE_TYPE_RESET_PAYPWD == msgType || UserConst.MSG_CODE_TYPE_SETTLE_PAYPWD == msgType) {
            userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        } else {
            throw new BusinessException("短信验证码类型有误");
        }

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
    public Result<?> settlePayPassword(HttpServletRequest request, User userReq) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        Integer msgType = UserConst.MSG_CODE_TYPE_SETTLE_PAYPWD;

        userSendService.validateMsgCode(userId, userReq.getPhoneNumber(), userReq.getMsgCode(), msgType);
        userService.resetPassword(userId, userReq.getPayPassword(), msgType);

        return ResultUtils.success("设置支付密码成功");
    }

    /**
     * 修改密码
     *
     * @param request
     * @param userReq
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("pwd/update")
    public Result<?> updatePassword(HttpServletRequest request, User userReq) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        userService.updatePassword(userId, userReq.getOldPassword(), userReq.getNewPassword(), userReq.getUpdatePwdType());
        return ResultUtils.success("修改密码成功");
    }

    /**
     * 首页算力排行榜
     *
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
        userComputePowerRank.setUserAssetsList((List<UserAssets>) redisClient.listRange(RedisKeyConst.COMPUTE_POWER_RANK_LIST, 0, 9));

        return ResultUtils.success(userComputePowerRank);
    }

    /**
     * 附近的人
     *
     * @param request
     * @param userLocation
     * @return
     * @throws UnsupportedEncodingException
     */
    @ResponseBody
    @RequestMapping(value = "nearByUserList")
    public Result<?> nearByUserList(HttpServletRequest request, UserLocation userLocation) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        UserAssets userAssets = userAssetsServices.getAssetsByUserId(userId);
        ValueCheckUtils.notEmpty(userAssets, "未找到用户资产");
        return ResultUtils.success(userService.nearUserList(userId, userLocation.getProvinceCode(),
                userLocation.getCityCode(), userLocation.getRegionCode(), userLocation.getStreetCode(),
                userLocation.getCoordinateX(), userLocation.getCoordinateY(), userAssets.getComputePower()));
    }

    /**
     * 市内小样机
     * @param request
     * @param sl
     * @return
     * @throws UnsupportedEncodingException
     */
    @ResponseBody
    @RequestMapping(value = "citySampleMachine")
    public Result<?> citySampleMachine(HttpServletRequest request, SampleMachineLocation sl) throws UnsupportedEncodingException {
        List<SampleMachineLocation> slList = sampleMachineService.listSampleMachineLocation(sl.getCityCode(),sl.getCoordinateX(),sl.getCoordinateY());
        return ResultUtils.success(slList);
    }
}
