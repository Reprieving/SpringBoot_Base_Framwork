package com.balance.controller.app;

import com.balance.architecture.dto.Result;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.utils.ResultUtils;
import com.balance.entity.user.User;
import com.balance.service.user.CertificationService;
import com.balance.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("app/user")
public class AppUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CertificationService certificationService;

    @RequestMapping("register")
    public Result<?> register(User user) throws BusinessException {
        userService.createUser(user);
        return ResultUtils.success("注册成功");
    }

    @RequestMapping("login")
    public Result<?> login(User user) throws BusinessException, UnsupportedEncodingException {
        String loginToken = userService.login(user);
        return ResultUtils.success(loginToken,"登录成功");
    }

    @RequestMapping("cert")
    public Result<?> cert(HttpServletRequest request, User user, @RequestParam("file") MultipartFile file) throws BusinessException, UnsupportedEncodingException {
        return ResultUtils.success("");
    }

}
