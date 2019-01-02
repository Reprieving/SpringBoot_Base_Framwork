package com.balance.mapper.user;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFreeCountMapper {
    Integer updateUserSendMsgCount(@Param("userId") String userId);

    Integer updateUserFreeLuckDrawCount(@Param("userId")String userId);

    Integer updateUserShareCount(@Param("userId")String userId);
}
