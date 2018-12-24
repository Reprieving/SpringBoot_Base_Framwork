package com.balance.mapper.user;

import com.balance.entity.user.UserVoucherRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserVoucherMapper {

    /**
     * 查询用户指定的卡券信息
     * @param userId
     * @return
     */
    List<UserVoucherRecord> listUserVoucher(@Param("userId") String userId,@Param("ifValid") Integer ifValid);

    /**
     * 查找用户指定的卡券信息
     * @param userId
     * @param voucherId
     * @return
     */
    UserVoucherRecord getUserVoucher(@Param("userId")String userId, @Param("voucherId")String voucherId);

    /**
     * 扣除用户的卡券数量
     * @param userVoucherRecordId
     * @return
     */
    Integer decreaseQuantity(@Param("userVoucherRecordId") String userVoucherRecordId,@Param("version") Integer version);
}
