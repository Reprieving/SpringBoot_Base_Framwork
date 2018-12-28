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
    List<OrderGoodsInfo> listUserOrderGoodsByStatus(@Param("userId") String userId, @Param("orderStatus") Integer orderStatus, @Param("pagination") Pagination pagination);

    /**
     * 用户订单详情
     *
     * @param orderId
     * @return
     */
    OrderGoodsInfo getUserOrderGoodsInfo(@Param("userId") String userId,@Param("orderId") String orderId);

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
                                                 @Param("status") Integer status, @Param("pagination") Pagination pagination);

    /**
     * 扫码领取小样订单
     *
     * @param userId
     * @param ifScan
     * @return
     */
    List<OrderGoodsInfo> listUserBeautyGoodsInfo(@Param("userId") String userId, @Param("ifScan") Boolean ifScan);


    /**
     * 设置订单为已支付状态
     * @param orderId
     * @param userId
     * @return
     */
    Integer updateOrderHadPay(@Param("orderId")String orderId, @Param("userId")String userId);
}
