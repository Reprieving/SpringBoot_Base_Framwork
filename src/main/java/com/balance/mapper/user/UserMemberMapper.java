package com.balance.mapper.user;

import com.balance.entity.user.UserMemberRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMemberMapper {

    /**
     * 查询逾期的会员记录
     *
     * @param expireTime
     * @return
     */
    List<UserMemberRecord> listExpireMember(@Param("expireTime") String expireTime);

    /**
     * 作废逾期的会员记录
     * @param memberRecordIds
     */
    void updateMemberRecord(@Param("ids")List<String> memberRecordIds);

    /**
     * 更新用户的会员类型
     * @param userIds
     * @param memberType
     */
    void updateUserMemberType(@Param("ids")List<String> userIds, @Param("memberType")Integer memberType);
}
