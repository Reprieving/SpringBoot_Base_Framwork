package com.balance.mapper.user;

import com.balance.entity.user.UserAssets;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface UserAssetsMapper {

    /**
     * 更改用户资产
     * @param userId 用户id
     * @param amount 数目（正数为加，负数为减）
     * @return
     */
    Integer updateUserAssets(@Param("userId") String userId, @Param("amount") BigDecimal amount, @Param("assetColumn") String assetColumn,@Param("version") Integer version);

    /**
     * 更改用户冻结资产
     * @param userId 用户id
     * @param amount 数目（正数为加，负数为减）
     * @param version
     * @return
     */
    Integer updateUserFrozenAssets(@Param("userId") String userId, @Param("amount") BigDecimal amount, @Param("assetColumn") String assetColumn,@Param("version") Integer version);

    /**
     * 查询所有正常用户的算力
     * @return
     * @param status 用户正常状态
     */
    List<UserAssets> listComputePower(@Param("status")Integer status);
}
