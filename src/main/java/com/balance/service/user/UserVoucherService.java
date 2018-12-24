package com.balance.service.user;

import com.balance.architecture.service.BaseService;
import com.balance.entity.user.UserAdvertisement;
import com.balance.entity.user.UserVoucherRecord;
import com.balance.mapper.user.UserMapper;
import com.balance.mapper.user.UserVoucherMapper;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Service
public class UserVoucherService extends BaseService {

    @Autowired
    private UserVoucherMapper userVoucherMapper;

    /**
     * 获取用户拥有的卡券
     *
     * @param userId
     * @param ifValid
     * @return
     */
    public List<UserVoucherRecord> listUserVoucher(String userId, Integer ifValid) {
        return userVoucherMapper.listUserVoucher(userId,ifValid);
    }

}
