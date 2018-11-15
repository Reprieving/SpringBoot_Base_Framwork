package com.balance.controller.app;

import com.balance.architecture.dto.Result;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.utils.ResultUtils;
import com.balance.entity.user.User;
import com.balance.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("app/user")
public class AppUserController {

    @Autowired
    private UserService userService;


    @RequestMapping("register")
    public Result<?> register(User user) throws BusinessException {
        user.setUserName("1");
        userService.createUser(user);
        return ResultUtils.success("注册成功");
    }

    @RequestMapping("login")
    public Result<?> login(User user) throws BusinessException {
        String loginToken = userService.login(user);
        return ResultUtils.success("登录成功");
    }

}
