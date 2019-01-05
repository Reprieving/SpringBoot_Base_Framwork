package com.balance.service.user;

import com.balance.architecture.service.BaseService;
import com.balance.client.RedisClient;
import com.balance.constance.RedisKeyConst;
import com.balance.constance.ShopConst;
import com.balance.entity.shop.GoodsSpu;
import com.balance.entity.user.UserVoucherRecord;
import com.balance.mapper.user.UserVoucherMapper;
import com.balance.service.common.GlobalConfigService;
import com.balance.service.shop.ShopVoucherService;
import com.balance.utils.ValueCheckUtils;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserVoucherService extends BaseService {


    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private ShopVoucherService shopVoucherService;

    @Autowired
    private UserVoucherMapper userVoucherMapper;

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private GlobalConfigService globalConfigService;

    /**
     * 获取用户拥有的卡券
     *
     * @param userId
     * @return
     */
    public Map<String, Object> listUserVoucher(String userId) {
        List<UserVoucherRecord> userVoucherRecords = userVoucherMapper.listUserVoucher(userId);
        List<UserVoucherRecord> ableDataList = new ArrayList<>(userVoucherRecords.size());
        List<UserVoucherRecord> unableDataList = new ArrayList<>(userVoucherRecords.size());
        userVoucherRecords.forEach(userVoucherRecord -> {
            if (userVoucherRecord.ifValid) {
                ableDataList.add(userVoucherRecord);
            } else {
                unableDataList.add(userVoucherRecord);
            }
        });
        return ImmutableMap.of(ShopConst.VOUCHER_ABLEDATALIST_KEY, ableDataList, ShopConst.VOUCHER_UNABLEDATALIST_KEY, unableDataList);
    }

    /**
     * 获取商品对应的卡券
     *
     * @param userId
     * @return
     */
    public Map<String, Object> listGoodsVoucher(String userId, String spuId) {
        GoodsSpu goodsSpu = selectOneById(spuId, GoodsSpu.class);
        ValueCheckUtils.notEmpty(goodsSpu, "未找到商品");
        Integer spuType = goodsSpu.getSpuType();
        List<UserVoucherRecord> userVoucherRecords = userVoucherMapper.listGoodsVoucher(userId);
        List<UserVoucherRecord> ableDataList = new ArrayList<>(userVoucherRecords.size());
        List<UserVoucherRecord> unableDataList = new ArrayList<>(userVoucherRecords.size());

        switch (spuType) {
            case ShopConst.SPU_TYPE_PACKAGE:
                userVoucherRecords.forEach(ur -> {
                    if (ShopConst.VOUCHER_TYPE_BEAUTY_PACKAGE_DEDUCTION == ur.getVoucherType()) {
                        ableDataList.add(ur);
                    } else {
                        unableDataList.add(ur);
                    }
                });
                break;

            case ShopConst.SPU_TYPE_BIRTHDAY:
                userVoucherRecords.forEach(ur -> {
                    if (ShopConst.VOUCHER_TYPE_BIRTH_PACKAGE_DEDUCTION == ur.getVoucherType()) {
                        ableDataList.add(ur);
                    } else {
                        unableDataList.add(ur);
                    }
                });
                break;

            default:

        }

        return ImmutableMap.of(
                ShopConst.VOUCHER_ABLEDATALIST_KEY, ableDataList, ShopConst.VOUCHER_UNABLEDATALIST_KEY, unableDataList,
                "redirectUrl", globalConfigService.get(GlobalConfigService.Enum.BEAUTY_PACKAGE_VOUCHER_BUY_REDIRECT)
        );
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
                redisClient.set(RedisKeyConst.USE_VOUCHER_FLAG, "0");

                List<String> ids = new ArrayList<>();
                userVoucherMapper.listExpireVoucher(expireTime).forEach(e -> {
                    ids.add(e.getId());
                });
                if (ids.size() > 0) {
                    userVoucherMapper.updateExpireVoucher(ids);
                }

                redisClient.set(RedisKeyConst.USE_VOUCHER_FLAG, "1");
            }
        });
    }

}
