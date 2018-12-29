package com.balance.controller.app;

import com.balance.service.shop.OrderNotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("app/weChatPayNotify")
public class AppWeChatPayNotifyController {

    @Autowired
    private OrderNotifyService orderNotifyService;


    @RequestMapping("receiveBeautyNotify")
    @ResponseBody
    public String receiveBeautyNotify(HttpServletRequest request) throws IOException {
        return orderNotifyService.receiveBeautyWeChatPayNotify(request);
    }

    @RequestMapping("receiveBeautyNotify")
    @ResponseBody
    public String becomeMemberNotify(HttpServletRequest request) throws IOException {
        return orderNotifyService.becomeMemberWeChatPayNotify(request);
    }
}
