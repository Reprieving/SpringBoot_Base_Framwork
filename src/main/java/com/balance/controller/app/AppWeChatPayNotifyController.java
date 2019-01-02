package com.balance.controller.app;

import com.balance.constance.ShopConst;
import com.balance.service.shop.OrderNotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping(ShopConst.WeChatPayNotifyController)
public class AppWeChatPayNotifyController {

    @Autowired
    private OrderNotifyService orderNotifyService;

    //线上领取小样微信支付回调
    @RequestMapping(ShopConst.WeChatPayReceiveBeautyNotify)
    @ResponseBody
    public String receiveBeautyNotify(HttpServletRequest request) throws IOException {
        return orderNotifyService.receiveBeautyWeChatPayNotify(request);
    }

    //办理年卡会员微信支付回调
    @RequestMapping(ShopConst.WeChatPayBecomeMemberNotify)
    @ResponseBody
    public String becomeMemberNotify(HttpServletRequest request) throws IOException {
        return orderNotifyService.becomeMemberWeChatPayNotify(request);
    }


}
