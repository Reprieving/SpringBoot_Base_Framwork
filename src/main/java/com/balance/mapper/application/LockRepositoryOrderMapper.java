package com.balance.mapper.application;

import com.balance.entity.application.LockRepositoryOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LockRepositoryOrderMapper {

    /**
     * 查询可发放收益的锁仓订单
     *
     * @param expireTime
     * @return
     */
    List<LockRepositoryOrder> listLockRepOrderToReward(@Param("expireTime") String expireTime);

    /**
     * 批量更新锁仓订单为已收益
     * @param updateOrderIds
     */
    void updateLockRepositoryToReceive(@Param("updateOrderIds")List<String> updateOrderIds,@Param("receiveStatus")Integer receiveStatus);
}
