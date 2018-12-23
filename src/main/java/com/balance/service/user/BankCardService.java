package com.balance.service.user;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.constance.CommonConst;
import com.balance.constance.UserConst;
import com.balance.entity.user.BankCard;
import com.balance.entity.user.BankInfo;
import com.balance.entity.user.User;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by weihuaguo on 2018/12/23 15:44.
 */
@Service
public class BankCardService extends BaseService {

    @Autowired
    private UserSendService userSendService;

    public List<BankCard> getList(String userId, Pagination pagination) {
        Map<String, Object> orderMap = ImmutableMap.of(BankCard.Create_time, CommonConst.MYSQL_DESC);
        List<BankCard> bankCards = selectListByWhereString(BankCard.User_id + " = ", userId, pagination, BankCard.class, orderMap);
        if (CollectionUtils.isEmpty(bankCards)) {
            return Collections.EMPTY_LIST;
        }
        List<BankInfo> bankInfoList = selectAll(null, BankInfo.class);
        for (BankCard bankCard : bankCards) {
            String number = bankCard.getNumber();
            int beginIndex = number.length() - 3;
            bankCard.setNumber(number.substring(0, 4) + " **** **** " + number.substring(beginIndex, (beginIndex + 3)));
            for (BankInfo bankInfo : bankInfoList) {
                if (bankInfo.getName().equals(bankCard.getName())) {
                    bankCard.setIcon(bankInfo.getIcon());
                    break;
                }
            }
        }
        return bankCards;
    }

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

    public void remove(BankCard bankCard) {
        BankCard selectBank = selectOneById(bankCard.getId(), BankCard.class);
        if (selectBank == null || !selectBank.getUserId().equals(bankCard.getUserId())) {
            throw new BusinessException("数据异常");
        }
        if (delete(bankCard) < 1) {
            throw new BusinessException("删除失败");
        }
    }


}
