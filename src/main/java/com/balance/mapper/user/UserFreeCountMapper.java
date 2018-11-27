package com.balance.mapper.user;

import org.springframework.stereotype.Repository;

@Repository
public interface UserFreeCountMapper {
    Integer updateUserSendMsgCount(String userId);

    Integer updateUserFreeLuckDrawCount(String userId);
}
