package com.balance.controller.admin;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.dto.Result;
import com.balance.architecture.utils.ResultUtils;
import com.balance.entity.shop.OrderGoodsInfo;
import com.balance.entity.shop.OrderInfo;
import com.balance.service.shop.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("admin/order")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;


    /**
     * 条件查询订单列表
     *
     * @param orderInfo 条件查询实体
     * @return
     */
    @RequestMapping("list")
    public Result<?> list(@RequestBody OrderInfo orderInfo) throws UnsupportedEncodingException {
        Pagination pagination = orderInfo.getPagination();
        List<OrderGoodsInfo> orderInfoList = orderService.listAdminOrderGoodsInfo(orderInfo, pagination);
        return ResultUtils.success(orderInfoList);
    }
}
