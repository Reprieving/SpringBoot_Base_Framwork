package com.balance.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.balance.architecture.dto.Pagination;
import com.balance.architecture.dto.Result;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.utils.JwtUtils;
import com.balance.architecture.utils.ResultUtils;
import com.balance.architecture.utils.ValueCheckUtils;
import com.balance.client.RedisClient;
import com.balance.constance.RedisKeyConst;
import com.balance.constance.UserConst;
import com.balance.controller.app.req.PaginationReq;
import com.balance.controller.app.req.UserLocation;
import com.balance.entity.shop.SampleMachineLocation;
import com.balance.entity.user.*;
import com.balance.service.common.AddressService;
import com.balance.service.common.WjSmsService;
import com.balance.service.shop.SampleMachineService;
import com.balance.service.user.*;
import com.balance.utils.RandomUtil;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.Timestamp;
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
    private UserMemberService userMemberService;

    @Autowired
    private CertificationService certificationService;

    @Autowired
    private SampleMachineService sampleMachineService;

    @Autowired
   private RedisClient redisClient;

    @Autowired
    private AddressService addressService;

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
        userSendService.validateMsgCode(user.getUserId(), user.getPhoneNumber(), user.getMsgCode(), UserConst.MSG_CODE_TYPE_LOGINANDREGISTER);
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
    public Result<?> updateInfo(HttpServletRequest request, String userName, Integer sex, String location, Timestamp birthday) throws BusinessException, UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        User user = new User();
        user.setId(userId);
        if (StringUtils.isNotBlank(userName)) {
            user.setUserName(userName);
            userService.updateUserName(user);
        } else if(sex != null && (sex == 1 || sex == 2)){
            user.setSex(sex);
        } else if(StringUtils.isNotBlank(location)) {
            user.setLocation(addressService.getLocation(location));
        } else if(birthday != null) {
            user.setBirthday(birthday);
        } else {
            return ResultUtils.error("缺少更新数据");
        }
        userService.updateIfNotNull(user);
        return ResultUtils.success("修改成功");
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
            userId = user.getUserId();
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
        return ResultUtils.success(userService.nearUserList(
                userId, userLocation.getProvinceCode(),
                userLocation.getCityCode(), userLocation.getRegionCode(), userLocation.getStreetCode(),
                userLocation.getCoordinateX(), userLocation.getCoordinateY(), userAssets.getComputePower())
        );
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

    /**
     * 获取省市联动数据
     * @param pid
     * @return
     */
    @GetMapping("area/{pid}")
    public Result<?> area(@PathVariable Integer pid) {
        return ResultUtils.success(addressService.getByPid(pid));
    }

    /**
     * 绑定第三方
     * @param type
     * @param openId
     * @return
     */
    @GetMapping("binding/{type}/{openId}")
    public Result<?> binding(@PathVariable String type, @PathVariable String openId, HttpServletRequest request) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        User user = userService.selectOneById(userId, User.class);
        int result = 0;
        switch (type) {
            case "wx" :
                String oldOpenId = user.getWxOpenId();
                if(StringUtils.isNotBlank(oldOpenId) && oldOpenId.equals(openId)) {
                    return ResultUtils.error("不能绑定同一个微信");
                }
                if(userService.selectOneByWhereString(User.Wx_open_id + " = ", openId, User.class) != null) {
                    return ResultUtils.error("该微信已经绑定其它账号");
                }
                user = new User();
                user.setId(userId);
                user.setWxOpenId(openId);
                result = userService.updateIfNotNull(user);
                break;
        }
        if (result > 0) {
            return ResultUtils.success();
        } else {
            return ResultUtils.error("绑定失败");
        }
    }

    /**
     * 解绑第三方
     * @param type
     * @return
     */
    @GetMapping("unbind/{type}")
    public Result<?> unbind(@PathVariable String type, HttpServletRequest request) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        User user = userService.selectOneById(userId, User.class);
        int result = 0;
        switch (type) {
            case "wx" :
                if (StringUtils.isBlank(user.getWxOpenId())) {
                    return ResultUtils.error("您还绑定微信");
                }
                User update = new User();
                update.setWxOpenId(StringUtils.EMPTY);
                userService.updateIfNotNull(update);
                break;
        }
        if (result > 0) {
            return ResultUtils.success();
        } else {
            return ResultUtils.error("解绑成功");
        }
    }

    /**
     * 检查短信验证码有效性
     * @param request
     * @param type
     * @param msgCode
     * @return
     * @throws UnsupportedEncodingException
     */
    @GetMapping("checkMsgCode")
    public Result<?> checkMsgCode(HttpServletRequest request, Integer type, String msgCode) throws UnsupportedEncodingException {
        if(type == UserConst.MSG_CODE_TYPE_UNBIND_PHONE) {
            String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
            User user = userService.selectOneById(userId, User.class);
            String phoneNumber = user.getPhoneNumber();
            if (StringUtils.isBlank(phoneNumber)) {
                return ResultUtils.error("您还没有绑定手机号码");
            }
            userSendService.validateMsgCode(userId, phoneNumber, msgCode, UserConst.MSG_CODE_TYPE_UNBIND_PHONE);
            return ResultUtils.success();
        }
        return ResultUtils.error("短信验证错误");
    }

    /**
     * 绑定新手机号码
     * @param request
     * @param msgCode
     * @param phoneNumber
     * @return
     * @throws UnsupportedEncodingException
     */
    @PostMapping("bindPhone")
    public Result<?> bindPhone(HttpServletRequest request, String msgCode, String phoneNumber) throws UnsupportedEncodingException {
        if (StringUtils.isBlank(msgCode) || StringUtils.isBlank(phoneNumber)) {
            return ResultUtils.error("缺少必要参数");
        }
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        User user = userService.selectOneByWhereString(User.Phone_number + " = ", phoneNumber, User.class);
        if (user != null) {
            return ResultUtils.error("该手机号码已经绑定其它账号");
        }
        userSendService.validateMsgCode(userId, phoneNumber, msgCode, UserConst.MSG_CODE_TYPE_BIND_PHONE);
        User update = new User();
        update.setId(userId);
        update.setPhoneNumber(phoneNumber);
        userService.updateIfNotNull(update);
        return ResultUtils.success();
    }

    /**
     * 办理年卡会员
     * @param request
     * @return
     */
    @RequestMapping("member/become")
    public Result<?> becomeMember(HttpServletRequest request) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        userMemberService.becomeMember(userId,UserConst.USER_MEMBER_TYPE_COMMON);
        return ResultUtils.success("办理年卡会员成功");
    }

    /**
     * 公告和广告图
     * @param request
     * @param pagination
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("announceAndAd")
    public Result<?> announceAndAd(HttpServletRequest request,Pagination pagination) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        return ResultUtils.success(userService.listAnnounceAndAd(pagination));
    }

    /**
     * wx用户同步数据
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("synchronizing/wx")
    public Result<?> synchronizingWX(HttpServletRequest request,@RequestParam("userStr") String userStr){
        return ResultUtils.success();
    }

}
