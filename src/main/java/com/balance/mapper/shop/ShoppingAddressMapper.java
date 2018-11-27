package com.balance.mapper.shop;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingAddressMapper {

    void updateDefault(@Param("addressId") String addressId);
}
