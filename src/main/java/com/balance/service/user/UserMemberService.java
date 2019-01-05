package com.balance.service.user;

import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.client.RedisClient;
import com.balance.constance.*;
import com.balance.entity.common.GlobalConfig;
import com.balance.entity.mission.Mission;
import com.balance.entity.shop.ShopVoucher;
import com.balance.entity.user.*;
import com.balance.mapper.common.GlobalConfigMapper;
import com.balance.mapper.user.UserMemberMapper;
import com.balance.service.common.GlobalConfigService;
import com.balance.service.mission.MissionService;
import com.balance.service.shop.OrderService;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.time.DateUtils;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserMemberService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(UserMemberService.class);

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private MissionService missionService;

    @Autowired
    private UserAssetsService userAssetsService;

    @Autowired
    private UserMemberMapper userMemberMapper;

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private GlobalConfigService globalConfigService;

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
                ShopVoucher beautyVoucher = selectOneByWhereMap(beautyMap, ShopVoucher.class);
                UserVoucherRecord beautyUr = new UserVoucherRecord(userId, 1, beautyVoucher.getId(), unableTime);

                Integer beautyVoucherCount = Integer.valueOf(globalConfigService.get(GlobalConfigService.Enum.BECOME_MEMBER_BEAUTY_PACKAGE_VOUCHER_COUNT));
                for (int i = 0; i < beautyVoucherCount; i++) {
                    insertIfNotNull(beautyUr);
                }

                //生日礼包
                Map<String, Object> birthMap = ImmutableMap.of(ShopVoucher.Shop_id + "=", ShopConst.SHOP_OFFICIAL, ShopVoucher.Voucher_type + "=", ShopConst.VOUCHER_TYPE_BIRTH_PACKAGE_DEDUCTION);
                ShopVoucher birthVoucher = selectOneByWhereMap(birthMap, ShopVoucher.class);
                UserVoucherRecord birthUr = new UserVoucherRecord(userId, 1, birthVoucher.getId(), unableTime);
                insertIfNotNull(birthUr);

                //完成任务
                missionService.finishMission(userId, MissionConst.APPLY_YEAR_CARD, "办理年卡");

                //年卡分润
                userAssetsService.updateRMBAssetsByTurnoverType(user, AssetTurnoverConst.TURNOVER_TYPE_MEMBER_BECOME, "邀请的用户办理年卡分润");
            }
        });
    }


    /**
     * 取消逾期的会员
     *
     * @param expireTime
     */
    public void cancelExpireMember(String expireTime) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                redisClient.set(RedisKeyConst.BECOME_MEMBER_FLAG, "0");

                List<UserMemberRecord> userMemberRecords = userMemberMapper.listExpireMember(expireTime);
                List<String> memberRecordIds = new ArrayList<>(userMemberRecords.size());
                List<String> userIds = new ArrayList<>(userMemberRecords.size());
                userMemberRecords.forEach(e -> {
                    memberRecordIds.add(e.getId());
                    userIds.add(e.getUserId());
                });

                if (memberRecordIds.size() > 0) {
                    userMemberMapper.updateMemberRecord(memberRecordIds);
                }

                if (userIds.size() > 0) {
                    userMemberMapper.updateUserMemberType(userIds, UserConst.USER_MEMBER_TYPE_NONE);
                }

                redisClient.set(RedisKeyConst.BECOME_MEMBER_FLAG, "1");
            }
        });
    }
}
