package com.balance.controller.app;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.dto.Result;
import com.balance.architecture.utils.JwtUtils;
import com.balance.architecture.utils.ResultUtils;
import com.balance.entity.shop.OrderGoodsInfo;
import com.balance.entity.shop.OrderInfo;
import com.balance.service.shop.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RequestMapping(value = "/app/order")
@RestController
public class AppOrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 按状态查询订单
     * @param request
     * @param orderStatus
     * @return
     */
    @RequestMapping("list/{orderStatus}")
    public Result<?> list(HttpServletRequest request, @PathVariable("orderStatus")Integer orderStatus, @RequestBody Pagination pagination) throws UnsupportedEncodingException {
//        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        String userId = "1";
        List<OrderGoodsInfo> orderInfoList = orderService.listOrderGoodsInfo(userId,orderStatus,pagination);
        return ResultUtils.success(orderInfoList);
    }

}
