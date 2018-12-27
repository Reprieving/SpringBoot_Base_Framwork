package com.balance.service.user;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.architecture.utils.ValueCheckUtils;
import com.balance.constance.CommonConst;
import com.balance.constance.SettlementConst;
import com.balance.constance.UserConst;
import com.balance.entity.user.User;
import com.balance.entity.user.UserMerchantRecord;
import com.balance.entity.user.UserMerchantRuler;
import com.balance.utils.BigDecimalUtils;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserMerchantService extends BaseService {

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private UserAssetsService userAssetsService;

    @Autowired
    private UserService userService;

    public Object operatorUserMerchantRuler(UserMerchantRuler userMerchantRuler, Integer operatorType) {
        Object o = null;
        switch (operatorType) {
            case CommonConst.OPERATOR_TYPE_INSERT: //添加
                o = "创建商户等级规则成功";
                if (save(userMerchantRuler) == 0) {
                    throw new BusinessException("创建商户等级规则失败");
                }
                break;

            case CommonConst.OPERATOR_TYPE_DELETE: //删除
                userMerchantRuler.setIfValid(false);
                o = "删除商户等级规则成功";
                if (updateIfNotNull(userMerchantRuler) == 0) {
                    throw new BusinessException("删除商户等级规则失败");
                }
                break;

            case CommonConst.OPERATOR_TYPE_QUERY_LIST: //查询列表
                o = list(userMerchantRuler, userMerchantRuler.getPagination());
                break;

            case CommonConst.OPERATOR_TYPE_QUERY_DETAIL: //详情
                o = detail(userMerchantRuler.getId());
                break;
        }
        return o;
    }

    public Integer save(UserMerchantRuler userMerchantRuler) {
        ValueCheckUtils.notEmpty(userMerchantRuler.getMachineRankName(), "商户等级名称不能为空");
        ValueCheckUtils.notEmpty(userMerchantRuler.getMachinePrice(), "小样机单价不能为空");
        ValueCheckUtils.notEmpty(userMerchantRuler.getMachineStartCount(), "小样机器数量开始值不能为空");
        ValueCheckUtils.notEmpty(userMerchantRuler.getMachineEndCount(), "小样机器数量结束值不能为空");
        ValueCheckUtils.notEmpty(userMerchantRuler.getContractYearTimes(), "签约年数不能为空");
        ValueCheckUtils.notEmpty(userMerchantRuler.getComputeRewardRate(), "颜值奖励百分比不能为空");
        ValueCheckUtils.notEmpty(userMerchantRuler.getBeautyServiceProfit(), "线上领取小样分红不能为空");
        ValueCheckUtils.notEmpty(userMerchantRuler.getBecomeMemberProfit(), "办理礼盒年分红不能为空");
        ValueCheckUtils.notEmpty(userMerchantRuler.getComputeReturnRate(), "线上颜值返现不能为空");

        if (BigDecimalUtils.ifNegative(userMerchantRuler.getMachinePrice())) {
            throw new BusinessException("小样机单价不能为负数");
        }
        if (userMerchantRuler.getMachineStartCount() < 0 || userMerchantRuler.getMachineEndCount() < 0 || userMerchantRuler.getContractYearTimes() < 0) {
            throw new BusinessException("小样机器数量开始值和结束值,签约年数,都不能为负数");
        }
        if (userMerchantRuler.getMachineStartCount().compareTo(userMerchantRuler.getMachineEndCount()) == 1) {
            throw new BusinessException("小样机器数量开始值不能大于结束值");
        }
        if (userMerchantRuler.getMachineStartCount().compareTo(userMerchantRuler.getMachineEndCount()) == 0) {
            throw new BusinessException("小样机器数量开始值不能等于结束值");
        }
        if (BigDecimalUtils.ifNegative(userMerchantRuler.getComputeRewardRate())) {
            throw new BusinessException("颜值奖励百分比不能为负数");
        }
        if (BigDecimalUtils.ifNegative(userMerchantRuler.getBeautyServiceProfit())) {
            throw new BusinessException("线上领取小样分红不能为负数");
        }
        if (BigDecimalUtils.ifNegative(userMerchantRuler.getBecomeMemberProfit())) {
            throw new BusinessException("办理礼盒年分红不能为负数");
        }
        if (BigDecimalUtils.ifNegative(userMerchantRuler.getComputeReturnRate())) {
            throw new BusinessException("线上颜值返现不能为负数");
        }


        String entityId = userMerchantRuler.getId();
        if (StringUtils.isNoneBlank(entityId)) {
            ValueCheckUtils.notEmpty(selectOneById(entityId, UserMerchantRuler.class), "未找到商户规则记录");
            return updateIfNotNull(userMerchantRuler);
        } else {
            return insertIfNotNull(userMerchantRuler);
        }
    }


    public List list(UserMerchantRuler userMerchantRuler, Pagination pagination) {
        Class clazz = UserMerchantRuler.class;
        String machineRankName = userMerchantRuler.getMachineRankName();
        Map<String, Object> whereMap;
        if (StringUtils.isNoneBlank(machineRankName)) {
            whereMap = ImmutableMap.of(
                    UserMerchantRuler.Machine_rank_name + " LIKE ", "%" + machineRankName + "%",
                    UserMerchantRuler.If_valid + "=", true);
        } else {
            whereMap = ImmutableMap.of(
                    UserMerchantRuler.If_valid + "=", true);
        }
        Map<String, Object> orderMap = ImmutableMap.of(UserMerchantRuler.Machine_end_count, CommonConst.MYSQL_ASC);
        return selectListByWhereMap(whereMap, pagination, clazz, orderMap);
    }

    public UserMerchantRuler detail(String entity) {
        return selectOneById(entity, UserMerchantRuler.class);
    }


    /**
     * 用户签约成为商户
     *
     * @param userId
     * @param merchantRankName
     */
    public void becomeMerchant(String userId, String merchantRankName, Integer machineCount) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(merchantRankName)) {
            throw new BusinessException("用户id或商户等级名字不能为空");
        }

        if (machineCount == null || machineCount < 1) {
            throw new BusinessException("小样机数量不能少于1");
        }

        User user = selectOneById(userId, User.class);
        ValueCheckUtils.notEmpty(user, "未找到该用户信息");

        UserMerchantRuler merchantRuler = filterMatcherMerchantRuler(machineCount);
        ValueCheckUtils.notEmpty(merchantRuler, "未找到匹配的商户节点规则");

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                //更改用户的用户类型
                User updateUser = new User();
                updateUser.setId(userId);
                updateUser.setType(UserConst.USER_MERCHANT_TYPE_BEING);
                if (updateIfNotNull(updateUser) == 0) {
                    throw new BusinessException("设置商户失败");
                }

                Long currentTimeMillis = System.currentTimeMillis();
                Timestamp createTimestamp = new Timestamp(currentTimeMillis);
                Timestamp expireTimestamp = new Timestamp(DateUtils.addYears(new Date(), merchantRuler.getContractYearTimes()).getTime());


                //增加用户签约商户记录
                UserMerchantRecord userMerchantRecord = new UserMerchantRecord(userId, merchantRankName, merchantRuler.getId(), createTimestamp, expireTimestamp, true);
                if (insertIfNotNull(userMerchantRecord) == 0) {
                    throw new BusinessException("设置商户失败");
                }


                BigDecimal totalPrice = BigDecimalUtils.multiply(merchantRuler.getMachinePrice(), new BigDecimal(machineCount));    //机器单价X租赁机器数量
                BigDecimal giftCompute = BigDecimalUtils.multiply(totalPrice, merchantRuler.getComputeRewardRate());                 //赠送用户颜值
                if (userAssetsService.changeUserAssets(userId, giftCompute, SettlementConst.SETTLEMENT_COMPUTING_POWER) == 0) {
                    throw new BusinessException("设置商户失败");
                }

                //颜值分润
//                List<User> allUser = userService.listUser4InviteRecord();
//                for(User user1:allUser){
//                    if(user1.getInviteId().equals(userId)){
//
//                    }
//                }

            }
        });
    }

    /**
     * 通过设置的机器数量获取匹配的商户规则
     *
     * @param machineCount
     * @return
     */
    public UserMerchantRuler filterMatcherMerchantRuler(Integer machineCount) {
        List<UserMerchantRuler> userMerchantRulers = list(null, null);
        for (UserMerchantRuler ur : userMerchantRulers) {
            if (machineCount <= ur.getMachineEndCount()) {
                return ur;
            }
        }
        return null;
    }

}
