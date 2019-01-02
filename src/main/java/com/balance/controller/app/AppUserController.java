package com.balance.controller.app;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.dto.Result;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.utils.JwtUtils;
import com.balance.architecture.utils.ResultUtils;
import com.balance.utils.TreeNodeUtils;
import com.balance.utils.ValueCheckUtils;
import com.balance.client.RedisClient;
import com.balance.constance.RedisKeyConst;
import com.balance.constance.UserConst;
import com.balance.controller.app.req.UserLocation;
import com.balance.entity.shop.SampleMachineLocation;
import com.balance.entity.user.*;
import com.balance.service.common.AddressService;
import com.balance.service.common.AppUpgradeService;
import com.balance.service.shop.SampleMachineService;
import com.balance.service.user.*;
import com.balance.utils.IPUtils;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private AppUpgradeService appUpgradeService;

    @Autowired
    private BankCardService bankCardService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private ThirdPartyService thirdPartyService;

    @Autowired
    private UserMerchantService userMerchantService;


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
        return ResultUtils.success(userService.createUser(user));
    }

    /**
     * 用户登录
     *
     * @param user
     * @return
     */
    @RequestMapping("login")
    public Result<?> login(User user, String code, HttpServletRequest request)  {
        User userInfo;
        if (code != null) {
            userInfo = thirdPartyService.wxLogin(code, IPUtils.getClientIP(request));
        } else {
            userInfo = userService.login(user);
        }
        return ResultUtils.success(userInfo, "登录成功");
    }

    /**
     * 用户节点下数据
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("treeDownNode/{memberType}")
    public Result<?> treeDownNode(HttpServletRequest request,@PathVariable Integer memberType) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();

        List<User> allUser = userService.listUser4InviteRecord(memberType);
        List<User> treeUser = new ArrayList<>();
        TreeNodeUtils.filterUserDownTreeNode(userId,allUser,treeUser);

        Map<String,Object> map = ImmutableMap.of("count",treeUser.size(),"dataList",treeUser);
        return ResultUtils.success(map);
    }

    /**
     * 修改用户信息
     *
     * @param request
     * @return
     * @throws BusinessException
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("info/update")
    public Result<?> updateInfo(HttpServletRequest request, String userName, Integer sex, Integer location, String birthday) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        userService.updateInfo(userName, sex, location, birthday, userId);
        return ResultUtils.success();
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
    @RequestMapping("inviteRecord/{inviteType}")
    public Result<?> inviteRecord(HttpServletRequest request,Pagination pagination,@PathVariable Integer inviteType) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        return ResultUtils.success(userService.listInviteUser(userId,inviteType,pagination));
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
     * @param frontFiles
     * @param backFiles
     * @return
     * @throws BusinessException
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("cert")
    public Result<?> cert(HttpServletRequest request,String realName,String licenseNumber, MultipartFile frontFiles,MultipartFile backFiles) throws BusinessException, UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        certificationService.createCert(userId, realName,licenseNumber,frontFiles,backFiles);
        return ResultUtils.success("申请实名认证成功");
    }

    /**
     * 重置密码
     *
     * @param userReq
     */
    @RequestMapping("pwd/reset")
    public Result<?> resetPassword(HttpServletRequest request, User userReq) throws UnsupportedEncodingException {
        userService.resetPassword(request,userReq.getNewPassword(),userReq.getPhoneNumber(),userReq.getMsgCode(),userReq.getMsgType());
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
     */
    @GetMapping("binding/{type}/{code}")
    public Result<?> binding(@PathVariable String type, @PathVariable String code, HttpServletRequest request) throws UnsupportedEncodingException {
        return ResultUtils.success(thirdPartyService.binding(type, code, JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId()));
    }


    /**
     * 解绑第三方
     */
    @GetMapping("unbind/{type}")
    public Result<?> unbind(@PathVariable String type, HttpServletRequest request) throws UnsupportedEncodingException {
        userService.unbind(type, JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId());
        return ResultUtils.success();
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
        userSendService.validateMsgCode(JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId(), msgCode, type);
        return ResultUtils.success();
    }

    /**
     * 绑定/更改 手机号码
     */
    @PostMapping("bindPhone")
    public Result<?> bindPhone(HttpServletRequest request, String msgCode, String phoneNumber, String userId) {
        if (StringUtils.isBlank(msgCode) || StringUtils.isBlank(phoneNumber)) {
            return ResultUtils.error("缺少必要参数");
        }
        if (StringUtils.isNotBlank(userId)) {
            return ResultUtils.success(thirdPartyService.loginBind(msgCode, phoneNumber, userId, IPUtils.getClientIP(request)), "登录成功");
        } else {
            userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
            userService.changePhone(msgCode, phoneNumber, userId);
            return ResultUtils.success();
        }
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
     * wx用户同步数据
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("wx/synchronizing")
    public Result<?> synchronizingWX(@RequestParam("userStr") String userStr){
        userService.synchronizingWX(userStr);
        return ResultUtils.success("数据同步成功");
    }

    /**
     * app 检查版本
     */
    @RequestMapping("checkVer")
    public Result<?> checkVersion(String device, String version) {
        if (StringUtils.isBlank(device) || StringUtils.isBlank(version)) {
            return ResultUtils.error("缺少必要参数");
        }
        return ResultUtils.success(appUpgradeService.checkVersion(device, version));
    }


    /**
     * 银行卡 列表
     */
    @GetMapping("bankInfo")
    public Result<?> bankInfo() {
        return ResultUtils.success(bankCardService.selectAll(null, BankInfo.class));
    }

    /**
     * 用户已绑定 银行卡 列表
     */
    @GetMapping("bankList")
    public Result<?> bankList(HttpServletRequest request, Pagination pagination) throws UnsupportedEncodingException {
        return ResultUtils.success(bankCardService.getList(
                JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId(),
                pagination
        ));
    }

    /**
     * 添加银行卡
     */
    @PostMapping("addBank")
    public Result<?> addBank(HttpServletRequest request, BankCard bankCard, String msgCode, String bankId) throws UnsupportedEncodingException {
        bankCard.setUserId(JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId());
        bankCardService.add(bankCard, msgCode, bankId);
        return ResultUtils.success();
    }

    /**
     * 删除银行卡
     */
    @GetMapping("removeBank/{cardId}")
    public Result<?> removeBank(HttpServletRequest request, @PathVariable String cardId) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        BankCard bankCard = new BankCard();
        bankCard.setUserId(userId);
        bankCard.setId(cardId);
        bankCardService.remove(bankCard);
        return ResultUtils.success();
    }

    /**
     * 提交 意见反馈
     */
    @PostMapping("addFeedback")
    public Result<?> addFeedback(HttpServletRequest request, String content, Integer type) throws UnsupportedEncodingException {
        Feedback feedback = new Feedback();
        feedback.setUserId(JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId());
        feedback.setType(type);
        feedback.setContent(content);
        feedbackService.add(feedback);
        return ResultUtils.success();
    }

    /**
     * 提现列表
     */
    @GetMapping("withdrawList")
    public Result<?> withdrawList(HttpServletRequest request, Pagination pagination) throws UnsupportedEncodingException {
        return ResultUtils.success(bankCardService.withdrawList(
                JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId(),
                pagination
        ));
    }

    /**
     * 提现申请
     */
    @PostMapping("withdrawApply")
    public Result<?> withdrawApply(HttpServletRequest request, String cardId, BigDecimal amount, String msgCode) throws UnsupportedEncodingException {
        if (StringUtils.isBlank(cardId) || StringUtils.isBlank(msgCode)) {
            return ResultUtils.error("缺少必要参数");
        }
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        userSendService.validateMsgCode(userId, msgCode, UserConst.MSG_CODE_TYPE_BANK_WITHDRAW);
        bankCardService.withdrawApply(userId, cardId, amount);
        return ResultUtils.success();
    }

    /**
     * 节点(商家)用户列表 用户select 选择
     */
    @GetMapping("merchant/list")
    public Result<?> getMerchants(HttpServletRequest request) {
        return ResultUtils.success(userMerchantService.list(new UserMerchantRuler(), null));
    }

    /**
     * 申请 节点(商家) 用户
     */
    @PostMapping("merchant/apply")
    public Result<?> merchantApply(HttpServletRequest request, String merchantRulerId,
                                   String fullName, String telephone, String email, String location) {
        UserMerchantApply userMerchantApply = new UserMerchantApply();
        userMerchantApply.setUserId(JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId());
        userMerchantApply.setMerchantRulerId(merchantRulerId);
        userMerchantApply.setFullName(fullName);
        userMerchantApply.setLocation(location);
        userMerchantApply.setTelephone(telephone);
        userMerchantApply.setEmail(email);
        userMerchantService.merchantApply(userMerchantApply);
        return ResultUtils.success();
    }


}
