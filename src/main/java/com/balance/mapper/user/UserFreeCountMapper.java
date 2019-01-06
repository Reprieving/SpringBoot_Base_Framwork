package com.balance.mapper.user;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFreeCountMapper {

    Integer updateUserFreeCount(@Param("userId") String userId, @Param("field") String field, @Param("maxCount") Integer maxCount);

    Integer initMaxCount(@Param("fields") List<String> fields);
}
