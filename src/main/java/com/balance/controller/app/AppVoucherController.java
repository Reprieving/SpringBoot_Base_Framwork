package com.balance.controller.app;

import com.balance.architecture.dto.Result;
import com.balance.architecture.utils.JwtUtils;
import com.balance.architecture.utils.ResultUtils;
import com.balance.service.user.UserVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("app/voucher")
public class AppVoucherController {

    @Autowired
    private UserVoucherService userVoucherService;

    /**
     * 获取用户有效的的卡券列表
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("listOnUser")
    public Result<?> listOnUser(HttpServletRequest request) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        return ResultUtils.success(userVoucherService.listUserVoucher(userId));
    }


    /**
     * 获取用户有效的的卡券列表
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("listOnGoodsSpu/{spuId}")
    public Result<?> listOnGoodsSpu(HttpServletRequest request,@PathVariable String spuId) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        return ResultUtils.success(userVoucherService.listGoodsVoucher(userId,spuId));
    }
}
