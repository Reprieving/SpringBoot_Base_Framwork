package com.balance.mapper.user;

import com.balance.architecture.dto.Pagination;
import com.balance.entity.user.AssetsTurnover;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface AssetsTurnoverMapper {

    List<AssetsTurnover> selectByPage(Pagination<AssetsTurnover> pagination);


    /**
     * 查询用户的账单记录
     *
     * @param userId       用户id
     * @param turnoverType 账单记录
     * @param pagination   分页对象
     * @return
     */
    List<AssetsTurnover> listUserTurnover(@Param("userId") String userId, @Param("turnoverType")Integer turnoverType, @Param("pagination")Pagination pagination);
}
