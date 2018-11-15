package com.balance.mapper.user;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    Integer selectUserByUserName(@Param("userName") String userName);
}
