package com.balance.mapper.user;

import com.balance.entity.user.UserMemberRecord;
import com.balance.entity.user.UserMerchantRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMerchantMapper {

    List<UserMerchantRecord> listExpireMerchant(@Param("expireTime") String expireTime);

    void updateMerchantRecord(@Param("ids") List<String> userMerchantIds);

    void updateUserUserType(@Param("ids")List<String> userIds, @Param("userType")Integer userType);
}
