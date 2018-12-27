package com.balance.mapper.user;

import com.balance.architecture.dto.Pagination;
import com.balance.entity.user.InviteUserRecord;
import com.balance.entity.user.User;
import com.balance.entity.user.UserVoucherRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

    /**
     * 获取所有用户（用于展示邀请记录）
     * @return
     */
    List<User> listUser4InviteRecord(@Param("memberType") Integer memberType);

    /**
     * 根据更新字段重置密码
     * @param userId 用户id
     * @param newPassword 新密码
     * @param updatePWDColumn 更新字段
     */
    Integer updatePassword(@Param("userId") String userId, @Param("newPassword") String newPassword, @Param("updatePWDColumn") String updatePWDColumn);

    /**
     * 根据更新字段查询用户
     * @param phoneNumber
     * @param updatePWDColumn
     * @param oldPassword
     * @return
     */
    User getUserToUpdatePwd(@Param("phoneNumber")String phoneNumber, @Param("updatePWDColumn")String updatePWDColumn, @Param("oldPassword") String oldPassword);

    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    User getUserInfo(@Param("userId")String userId);


    /**
     * 用户邀请列表
     * @param userId
     * @param inviteType
     * @param pagination
     * @return
     */
    List<InviteUserRecord> listInviteUser(@Param("userId")String userId, @Param("inviteType")Integer inviteType,@Param("pagination")Pagination pagination);

    /**
     * 删除微信信息
     * @param userId
     * @return
     */
    int deleteWeixin(String userId);
}
