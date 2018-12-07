package com.balance.mapper.shop;

import com.balance.architecture.dto.Pagination;
import com.balance.entity.shop.OrderGoodsInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMapper {

    List<OrderGoodsInfo> listOrderGoodsInfo(@Param("userId") String userId, @Param("orderStatus") Integer orderStatus, @Param("pagination") Pagination pagination);

    OrderGoodsInfo getOrderGoodsInfo(@Param("orderId") String orderId);
}
