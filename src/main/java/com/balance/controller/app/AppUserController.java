package com.balance.controller.app;

import com.balance.architecture.dto.Result;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.utils.JwtUtils;
import com.balance.architecture.utils.ResultUtils;
import com.balance.architecture.utils.ValueCheckUtils;
import com.balance.constance.UserConst;
import com.balance.entity.user.User;
import com.balance.service.common.WjSmsService;
import com.balance.service.user.CertificationService;
import com.balance.service.user.UserSendService;
import com.balance.service.user.UserService;
import com.balance.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@RestController
@RequestMapping("app/user")
public class AppUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserSendService userSendService;

    @Autowired
    private WjSmsService wjSmsService;

    @Autowired
    private CertificationService certificationService;

    /**
     * 发送短信
     * @param user
     * @return
     * @throws BusinessException
     */
    @RequestMapping("sendMsg4register")
    public Result<?> sendMsg4register(User user) throws BusinessException {
        String msgCode = RandomUtil.randomNumber(6);
        userSendService.createMsgRecord(user.getId(), user.getPhoneNumber(),msgCode, user.getMsgType());

        String msgTypeStr = "";
        switch (user.getMsgType()){
            case UserConst.MSG_CODE_TYPE_REGISTER:
                msgTypeStr = "[注册]";
                break;
            case UserConst.MSG_CODE_TYPE_BACK_LOGINPWD:
                msgTypeStr = "[找回登录密码]";
                break;
            case UserConst.MSG_CODE_TYPE_SETTLE_PAYPWD:
                msgTypeStr = "[设置支付密码]";
                break;
            case UserConst.MSG_CODE_TYPE_BACK_PAYPWD:
                msgTypeStr = "[找回支付密码]";
                break;
        }

        String content = "美妆连"+msgTypeStr+"验证码： " + msgCode +"，五分钟有效。【美妆连】";
        wjSmsService.sendSms(user.getPhoneNumber(),content);

        return ResultUtils.success("发送短信成功");
    }

    /**
     * 提交注册
     * @param user
     * @return
     * @throws BusinessException
     */
    @RequestMapping("register")
    public Result<?> register(User user) throws BusinessException {

        userSendService.validateMsgCode(user.getId(),user.getPhoneNumber(),user.getMsgCode(),UserConst.MSG_CODE_TYPE_REGISTER);

        userService.createUser(user);
        return ResultUtils.success("注册成功");
    }

    /**
     * 用户登录
     * @param user
     * @return
     * @throws BusinessException
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("login")
    public Result<?> login(User user) throws BusinessException, UnsupportedEncodingException {
        User userInfo = userService.login(user);
        return ResultUtils.success(userInfo,"登录成功");
    }

    /**
     * 申请实名认证
     * @param request
     * @param user
     * @param files
     * @return
     * @throws BusinessException
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("cert")
    public Result<?> cert(HttpServletRequest request, User user, @RequestParam("file") MultipartFile[] files) throws BusinessException, UnsupportedEncodingException {
        String userId =  JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();

        certificationService.createCert(userId,files);

        return ResultUtils.success("申请实名认证成功");
    }

    /**
     * 重置密码
     * @param request
     * @param userReq
     */
    public Result<?> resetPassword(HttpServletRequest request,@RequestBody User userReq) throws UnsupportedEncodingException {
        String userId =  JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();

        ValueCheckUtils.notEmpty(userReq.getPhoneNumber(),"手机号不能为空");
        ValueCheckUtils.notEmpty(userReq.getNewPassword(),"新密码不能为空");
        ValueCheckUtils.notEmpty(userReq.getMsgCode(),"短信验证码不能为空");
        ValueCheckUtils.notEmpty(userReq.getMsgType(),"短信类型不能为空");


        userSendService.validateMsgCode(userId,userReq.getPhoneNumber(),userReq.getMsgCode(),userReq.getMsgType());

        userService.updatePassword(userId,userReq.getNewPassword(),userReq.getMsgType());

        return ResultUtils.success("重置密码成功");

    }

    /**
     * 设置支付密码
     * @param request
     * @param userReq
     * @return
     * @throws UnsupportedEncodingException
     */
    public Result<?> settlePayPassword(HttpServletRequest request,@RequestBody User userReq) throws UnsupportedEncodingException {
        String userId =  JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        ValueCheckUtils.notEmpty(userReq.getPhoneNumber(),"手机号不能为空");
        ValueCheckUtils.notEmpty(userReq.getPayPassword(),"支付密码不能为空");
        ValueCheckUtils.notEmpty(userReq.getMsgCode(),"短信验证码不能为空");

        Integer msgType = UserConst.MSG_CODE_TYPE_SETTLE_PAYPWD;

        userSendService.validateMsgCode(userId,userReq.getPhoneNumber(),userReq.getMsgCode(),msgType);

        userService.updatePassword(userId,userReq.getNewPassword(),msgType);

        return ResultUtils.success("设置支付密码成功");
    }

}
