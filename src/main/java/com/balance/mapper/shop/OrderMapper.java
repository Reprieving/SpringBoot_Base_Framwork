package com.balance.mapper.shop;

import com.balance.entity.shop.OrderGoodsInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMapper {

    List<OrderGoodsInfo> listOrderGoodsInfo(@Param("userId") String userId, @Param("orderStatus")Integer orderStatus);

    OrderGoodsInfo getOrderGoodsInfo(@Param("userId")String orderId);
}
