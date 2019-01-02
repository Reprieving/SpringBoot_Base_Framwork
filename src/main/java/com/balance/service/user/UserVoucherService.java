package com.balance.service.user;

import com.balance.architecture.service.BaseService;
import com.balance.client.RedisClient;
import com.balance.constance.RedisKeyConst;
import com.balance.entity.user.UserAdvertisement;
import com.balance.entity.user.UserVoucherRecord;
import com.balance.mapper.common.GlobalConfigMapper;
import com.balance.mapper.user.UserMapper;
import com.balance.mapper.user.UserVoucherMapper;
import com.balance.service.common.GlobalConfigService;
import org.apache.ibatis.annotations.Param;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserVoucherService extends BaseService {


    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private UserVoucherMapper userVoucherMapper;

    @Autowired
    private RedisClient redisClient;

    /**
     * 获取用户拥有的卡券
     *
     * @param userId
     * @param ifValid
     * @return
     */
    public List<UserVoucherRecord> listUserVoucher(String userId, Integer ifValid) {
        return userVoucherMapper.listUserVoucher(userId, ifValid);
    }


    /**
     * 作废逾期卡券
     *
     * @param expireTime
     * @return
     */
    public void cancelExpireVoucher(String expireTime) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                redisClient.set(RedisKeyConst.USE_VOUCHER_FLAG,"0");

                List<String> ids = new ArrayList<>();
                userVoucherMapper.listExpireVoucher(expireTime).forEach(e -> {
                    ids.add(e.getId());
                });
                if (ids.size() > 0) {
                    userVoucherMapper.updateExpireVoucher(ids);
                }

                redisClient.set(RedisKeyConst.USE_VOUCHER_FLAG,"1");
            }
        });
    }

}
