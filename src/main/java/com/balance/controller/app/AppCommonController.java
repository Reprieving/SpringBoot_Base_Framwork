package com.balance.controller.app;

import com.balance.architecture.dto.Result;
import com.balance.architecture.utils.JwtUtils;
import com.balance.architecture.utils.ResultUtils;
import com.balance.service.user.ThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("app/common")
public class AppCommonController {

    @Autowired
    private ThirdPartyService thirdPartyService;

    /**
     * 增加分享次数
     *
     * @return
     */
    @RequestMapping("share/increase")
    public Result<?> increaseShareCount(HttpServletRequest request) {
        thirdPartyService.shareCountIncrease(JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId());
        return ResultUtils.success();
    }
}
