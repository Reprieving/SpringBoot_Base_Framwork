package com.balance.service.user;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.constance.SettlementConst;
import com.balance.mapper.user.BankWithdrawMapper;
import com.balance.utils.BigDecimalUtils;
import com.balance.utils.ValueCheckUtils;
import com.balance.constance.CommonConst;
import com.balance.constance.RedisKeyConst;
import com.balance.constance.UserConst;
import com.balance.entity.user.BankCard;
import com.balance.entity.user.BankInfo;
import com.balance.entity.user.BankWithdraw;
import com.balance.entity.user.User;
import com.balance.service.common.GlobalConfigService;
import com.balance.utils.MineDateUtils;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 银行卡 相关
 * Created by weihuaguo on 2018/12/23 15:44.
 */
@Service
public class BankCardService extends BaseService {

    @Autowired
    private GlobalConfigService globalConfigService;

    @Autowired
    private UserSendService userSendService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserAssetsService userAssetsService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private BankWithdrawMapper bankWithdrawMapper;

    /**
     * 用户 已经绑定 银行卡列表
     */
    public List<BankCard> getList(String userId, Pagination pagination) {
        Map<String, Object> orderMap = ImmutableMap.of(BankCard.Create_time, CommonConst.MYSQL_DESC);
        List<BankCard> bankCards = selectListByWhereString(BankCard.User_id + " = ", userId, pagination, BankCard.class, orderMap);
        if (CollectionUtils.isEmpty(bankCards)) {
            return Collections.EMPTY_LIST;
        }
        List<BankInfo> bankInfoList = selectAll(null, BankInfo.class);
        for (BankCard bankCard : bankCards) {
            bankCard.setNumber(hideNumber(bankCard.getNumber()));
            for (BankInfo bankInfo : bankInfoList) {
                if (bankInfo.getName().equals(bankCard.getName())) {
                    bankCard.setIcon(bankInfo.getIcon());
                    break;
                }
            }
        }
        return bankCards;
    }

    /**
     * 添加银行卡
     */
    public void add(BankCard bankCard, String msgCode, String bankId) {
        String userId = bankCard.getUserId();
        if (StringUtils.isBlank(bankCard.getNumber()) || StringUtils.isBlank(bankCard.getIdCard()) || StringUtils.isBlank(bankId)
                || StringUtils.isBlank(bankCard.getRealName()) || StringUtils.isBlank(msgCode)) {
            throw new BusinessException("缺少必要参数");
        }
        BankInfo bankInfo = selectOneById(bankId, BankInfo.class);
        if (bankInfo == null) {
            throw new BusinessException("暂时不能添加这个银行");
        }
        Map<String, Object> whereMap = ImmutableMap.of(BankCard.User_id + " = ", userId, BankCard.Number + " = ", bankCard.getNumber());
        if (selectOneByWhereMap(whereMap, BankCard.class) != null) {
            throw new BusinessException("您已经添加了该银行卡号");
        }
        userSendService.validateMsgCode(userId, selectOneById(userId, User.class).getPhoneNumber(), msgCode, UserConst.MSG_CODE_TYPE_BIND_BANK);
        bankCard.setName(bankInfo.getName());
        insertIfNotNull(bankCard);
    }

    /**
     * 删除银行卡
     */
    public void remove(BankCard bankCard) {
        BankCard selectBank = selectOneById(bankCard.getId(), BankCard.class);
        if (selectBank == null || !selectBank.getUserId().equals(bankCard.getUserId())) {
            throw new BusinessException("银行卡数据异常");
        }
        ValueCheckUtils.notZero(delete(bankCard), "删除失败");
    }

    /**
     * 用户 银行卡 提现列表
     */
    public List<BankWithdraw> withdrawList(String userId, Pagination pagination) {
        Map<String, Object> orderMap = ImmutableMap.of(BankCard.Create_time, CommonConst.MYSQL_DESC);
        List<BankWithdraw> bankWithdrawList = selectListByWhereString(BankWithdraw.User_id + " = ",
                userId, pagination, BankWithdraw.class, orderMap);
        if (CollectionUtils.isEmpty(bankWithdrawList)) {
            return Collections.EMPTY_LIST;
        }
        for (BankWithdraw bankWithdraw : bankWithdrawList) {
            bankWithdraw.setNumber(hideNumber(bankWithdraw.getNumber()));
        }
        return bankWithdrawList;
    }


    /**
     * 用户 申请银行卡提现
     */
    public void withdrawApply(String userId, String cardId, BigDecimal amount) {
        double lowest = globalConfigService.getDouble(GlobalConfigService.Enum.BANK_WITHDRAW_LOWEST);
        double amountDouble = amount.doubleValue();
        if (amountDouble < lowest) {
            throw new BusinessException("提现金额不能低于" + lowest + "元");
        }
        // 当天提现额度 校验
        String redisKey = RedisKeyConst.BANK_WITHDRAW_AMOUNT + userId;
        double dayAmount = NumberUtils.toDouble(
                stringRedisTemplate.opsForValue().get(redisKey), 0)
                + amountDouble;
        double highest = globalConfigService.getDouble(GlobalConfigService.Enum.BANK_WITHDRAW_HIGHEST);
        if (dayAmount > highest) {
            throw new BusinessException("今天提现不能超过" + highest + "元");
        }

        BankCard bankCard = selectOneById(cardId, BankCard.class);
        if (bankCard == null || !bankCard.getUserId().equals(userId)) {
            throw new BusinessException("银行卡数据异常");
        }
        BankWithdraw bankWithdraw = new BankWithdraw();
        BeanUtils.copyProperties(bankCard, bankWithdraw);
        bankWithdraw.setAmount(amount);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                //插入提现记录
                int result1 = insertIfNotNull(bankWithdraw);
                // 用户余额校验
                int result2 = userAssetsService.changeUserAssets(userId, BigDecimalUtils.transfer2Negative(amount), SettlementConst.SETTLEMENT_RMB);
                if (result1 < 1 || result2 < 1) {
                    throw new BusinessException("提现失败, 请稍后重试");
                }
            }
        });
        stringRedisTemplate.opsForValue().increment(redisKey, amountDouble);
        stringRedisTemplate.expire(redisKey, MineDateUtils.getDaySeconds(), TimeUnit.SECONDS);
    }

    /**
     * 后台 银行卡 提现列表
     */
    public List<BankWithdraw> listBankWithdraw(Pagination<BankWithdraw> pagination) {
        return bankWithdrawMapper.selectByPage(pagination);
    }

    /**
     * 更新 银行卡提现 状态
     */
    public void updateBankWithdraw(String id, int state) {
        BankWithdraw bankWithdraw = selectOneById(id, BankWithdraw.class);
        if (bankWithdraw == null || bankWithdraw.getState() != UserConst.WITHDRAW_STATE_PENDING) {
            throw new BusinessException("数据状态异常");
        }
        int result1;
        int result2;
        if (state == UserConst.WITHDRAW_STATE_PASSED) {
            //通过, 仅更新状态
            result2 = 1;
        } else if (state == UserConst.WITHDRAW_STATE_NOTPASS){
            //不通过, 退款
            result2 = userAssetsService.changeUserAssets(bankWithdraw.getUserId(), bankWithdraw.getAmount(), SettlementConst.SETTLEMENT_RMB);
        } else {
            throw new BusinessException("参数异常");
        }
        bankWithdraw.setState(state);
        result1 = bankWithdrawMapper.updateState(bankWithdraw);
        if (result1 < 1 || result2 < 1) {
            throw new BusinessException("操作失败");
        }
    }

    /**
     * 隐藏卡号
     *
     * @param number
     * @return
     */
    private String hideNumber(String number) {
        int beginIndex = number.length() - 3;
        return number.substring(0, 4) + " **** **** " + number.substring(beginIndex, (beginIndex + 3));
    }

}
