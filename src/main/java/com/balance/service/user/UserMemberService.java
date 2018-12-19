package com.balance.service.user;

import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.constance.ShopConst;
import com.balance.entity.shop.ShopVoucher;
import com.balance.entity.user.User;
import com.balance.entity.user.UserVoucherRecord;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class UserMemberService extends BaseService {
    @Autowired
    private TransactionTemplate transactionTemplate;

    /**
     * 办理会员
     * @param userId 用户id
     * @param memberType
     */
    public void becomeMember(String userId,Integer memberType){
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                //更改用户会员类型
                User user = new User();
                user.setId(userId);
                user.setMemberType(memberType);
                if(updateIfNotNull(user)==0){
                    throw new BusinessException("办理会员失败");
                }

                //增加礼券
                Map<String,Object> map = ImmutableMap.of(ShopVoucher.Shop_id+"=", ShopConst.SHOP_OFFICIAL,ShopVoucher.Voucher_type+"=",ShopConst.VOUCHER_TYPE_PACKAGE);
                ShopVoucher shopVoucher = selectOneByWhereMap(map,ShopVoucher.class);

                UserVoucherRecord ur = new UserVoucherRecord(userId,shopVoucher.getId());

                List<UserVoucherRecord> urs = Arrays.asList(ur,ur,ur,ur);
                if(insertBatch(urs,false)==0){
                    throw new BusinessException("办理会员失败");
                }

                //支付对接
            }
        });
    }
}
