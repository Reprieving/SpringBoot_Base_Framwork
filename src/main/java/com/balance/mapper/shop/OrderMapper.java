package com.balance.mapper.shop;

import com.balance.architecture.dto.Pagination;
import com.balance.entity.shop.OrderGoodsInfo;
import com.balance.entity.shop.OrderInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface OrderMapper {

    /**
     * 用户订单列表
     *
     * @param userId
     * @param orderStatus
     * @param pagination
     * @return
     */
    List<OrderGoodsInfo> listUserOrderGoodsInfo(@Param("userId") String userId, @Param("orderStatus") Integer orderStatus, @Param("pagination") Pagination pagination);

    /**
     * 用户订单详情
     *
     * @param orderId
     * @return
     */
    OrderGoodsInfo getUserOrderGoodsInfo(@Param("orderId") String orderId);

    /**
     * 后台条件查询订单列表
     *
     * @param orderNo        订单号
     * @param userName       用户名
     * @param settlementId   支付方式
     * @param logisticNumber 物流号
     * @param startTime      开始时间
     * @param endTime        结束时间
     * @param status         状态
     * @param pagination     分页对象
     * @return
     */
    List<OrderGoodsInfo> listAdminOrderGoodsInfo(@Param("orderNo") String orderNo, @Param("userName") String userName, @Param("settlementId") Integer settlementId,
                                                 @Param("logisticNumber") String logisticNumber, @Param("startTime") Timestamp startTime, @Param("endTime") Timestamp endTime,
                                                 @Param("status") String status, @Param("pagination") Pagination pagination);
}
