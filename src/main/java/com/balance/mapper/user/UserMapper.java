package com.balance.mapper.user;

import com.balance.entity.user.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

    /**
     * 获取所有用户（用于展示邀请记录）
     * @return
     */
    List<User> listUser4InviteRecord();

    /**
     *
     * @param userId
     * @param newPassword
     * @param updatePWDColumn
     */
    Integer updatePassword(@Param("userId") String userId, @Param("newPassword") String newPassword, @Param("updatePWDColumn") String updatePWDColumn);
}
