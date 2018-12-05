package com.balance.mapper.common;

import com.balance.entity.common.UserInviteCodeId;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AutoIncreaseIdMapper {
    Integer insertUserInviteCode(UserInviteCodeId userInviteCodeId);
}
