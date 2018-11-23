package com.balance.controller.app;

import com.balance.architecture.dto.Result;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.utils.ResultUtils;
import com.balance.constance.UserConst;
import com.balance.entity.user.User;
import com.balance.service.common.WjSmsService;
import com.balance.service.user.CertificationService;
import com.balance.service.user.UserSendService;
import com.balance.service.user.UserService;
import com.balance.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * 发送用户短信
     * @param user
     * @return
     * @throws BusinessException
     */
    @RequestMapping("sendMsg4register")
    public Result<?> sendMsg4register(User user) throws BusinessException {
        String msgCode = RandomUtil.randomNumber(6);
        userSendService.createMsgRecord(user.getId(), user.getPhoneNumber(),msgCode, UserConst.MSG_CODE_TYPE_REGISTER);

        String content = "美妆连注册验证码： " + msgCode +"，五分钟有效。【美妆连】";
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
     * @param file
     * @return
     * @throws BusinessException
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("cert")
    public Result<?> cert(HttpServletRequest request, User user, @RequestParam("file") MultipartFile file) throws BusinessException, UnsupportedEncodingException {

        return ResultUtils.success("");
    }



}
