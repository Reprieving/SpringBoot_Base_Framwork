package com.balance.mapper.user;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface UserAssetsMapper {

    /**
     * 更改用户资产
     * @param userId 用户id
     * @param amount 数目（正数为加，负数为减）
     * @return
     */
    Integer updateUserAssets(@Param("userId") String userId, @Param("amount") BigDecimal amount, @Param("assetColumn") String assetColumn,@Param("version") Long version);

}
