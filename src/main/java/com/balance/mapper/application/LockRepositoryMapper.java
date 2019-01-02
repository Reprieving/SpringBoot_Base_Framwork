package com.balance.mapper.application;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LockRepositoryMapper {

    /**
     * 查询可开放的锁仓产品
     *
     * @param startTime
     * @param noneStatus 未开始状态
     * @param openStatus 已开放状态
     * @return
     */
    void updateLockRepToOpen(@Param("startTime") String startTime, @Param("noneStatus") Integer noneStatus, @Param("openStatus") Integer openStatus);


    /**
     * 查询该结束的锁仓产品
     *
     * @param endTime
     * @param openStatus 已开放状态
     * @param endStatus  已结束状态
     * @return
     */
    void updateLockRepToClose(@Param("endTime") String endTime, @Param("openStatus") Integer openStatus, @Param("endStatus") Integer endStatus);



}
