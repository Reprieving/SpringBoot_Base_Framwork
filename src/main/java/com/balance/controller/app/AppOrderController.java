package com.balance.controller.app;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.dto.Result;
import com.balance.architecture.utils.JwtUtils;
import com.balance.architecture.utils.ResultUtils;
import com.balance.controller.app.req.PaginationReq;
import com.balance.controller.app.req.ShopOrderPayReq;
import com.balance.entity.shop.OrderGoodsInfo;
import com.balance.entity.shop.OrderInfo;
import com.balance.service.shop.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public Result<?> list(HttpServletRequest request, @PathVariable("orderStatus")Integer orderStatus, @RequestBody PaginationReq paginationReq) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        Pagination pagination = paginationReq.getPagination();
        List<OrderGoodsInfo> orderInfoList = orderService.listOrderGoodsInfo(userId,orderStatus,pagination);
        return ResultUtils.success(orderInfoList);
    }

    /**
     * 订单详情
     * @param request
     * @param orderId
     * @return
     */
    @RequestMapping("detail/{orderId}")
    public Result<?> detail(HttpServletRequest request, @PathVariable("orderId")String orderId){
        OrderGoodsInfo orderGoodsInfo = orderService.getOrderGoodsInfo(orderId);
        return ResultUtils.success(orderGoodsInfo);
    }


    /**
     * 商城下单
     * @param request
     * @param shopOrderPayReq
     * @return
     */
    @RequestMapping("create")
    public Result<?> create(HttpServletRequest request,ShopOrderPayReq shopOrderPayReq) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        orderService.createOrder(shopOrderPayReq.getOrderSkuReqList(),userId,shopOrderPayReq.getAddressId(),shopOrderPayReq.getSettlementId());
        return ResultUtils.success();
    }
}
