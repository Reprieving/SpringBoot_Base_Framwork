package com.balance.mapper.shop;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingAddressMapper {
    Integer updateNotDefault(@Param("addressId") String addressId, @Param("userId") String userId);

    Integer updateDefault(@Param("addressId")String addressId, @Param("userId")String userId);
}
