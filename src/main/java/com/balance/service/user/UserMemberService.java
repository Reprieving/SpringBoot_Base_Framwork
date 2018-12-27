package com.balance.service.user;

import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.constance.MissionConst;
import com.balance.constance.ShopConst;
import com.balance.entity.mission.Mission;
import com.balance.entity.shop.ShopVoucher;
import com.balance.entity.user.User;
import com.balance.entity.user.UserVoucherRecord;
import com.balance.service.mission.MissionService;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.time.DateUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

@Service
public class UserMemberService extends BaseService {
    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private MissionService missionService;

    /**
     * 办理年卡
     *
     * @param userId     用户id
     * @param memberType
     */
    public void becomeMember(String userId, Integer memberType) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                //更改用户会员类型
                User user = new User();
                user.setId(userId);
                user.setMemberType(memberType);
                if (updateIfNotNull(user) == 0) {
                    throw new BusinessException("办理会员失败");
                }

                Timestamp unableTime = new Timestamp(DateUtils.addYears(new Date(), 1).getTime());

                //小样礼包卡券
                Map<String, Object> beautyMap = ImmutableMap.of(ShopVoucher.Shop_id + "=", ShopConst.SHOP_OFFICIAL, ShopVoucher.Voucher_type + "=", ShopConst.VOUCHER_TYPE_BEAUTY_PACKAGE_DEDUCTION);
                Integer beautyVoucherCount = 3;
                ShopVoucher beautyVoucher = selectOneByWhereMap(beautyMap, ShopVoucher.class);
                UserVoucherRecord beautyUr = new UserVoucherRecord(userId, beautyVoucherCount, beautyVoucher.getId(), unableTime);

                //生日礼包
                Map<String, Object> birthMap = ImmutableMap.of(ShopVoucher.Shop_id + "=", ShopConst.SHOP_OFFICIAL, ShopVoucher.Voucher_type + "=", ShopConst.VOUCHER_TYPE_BIRTH_PACKAGE_DEDUCTION);
                Integer birthVoucherCount = 1;
                ShopVoucher birthVoucher = selectOneByWhereMap(birthMap, ShopVoucher.class);
                UserVoucherRecord birthUr = new UserVoucherRecord(userId, birthVoucherCount, birthVoucher.getId(), unableTime);

                if (insertIfNotNull(beautyUr) == 0 || insertIfNotNull(birthUr) == 0) {
                    throw new BusinessException("办理会员失败");
                }

                //完成任务
                missionService.finishMission(userId, MissionConst.APPLY_YEAR_CARD,"办理年卡");

                //支付对接


            }
        });
    }
}
