package com.balance.service.shop;

import com.balance.architecture.service.BaseService;
import com.balance.architecture.utils.ResultUtils;
import com.balance.constance.AssetTurnoverConst;
import com.balance.constance.ShopConst;
import com.balance.constance.UserConst;
import com.balance.entity.common.GlobalConfig;
import com.balance.entity.common.SystemExceptionRecord;
import com.balance.entity.common.WeChatPayNotifyRecord;
import com.balance.entity.shop.OrderInfo;
import com.balance.entity.shop.OrderItem;
import com.balance.entity.shop.ShopVoucher;
import com.balance.entity.user.User;
import com.balance.entity.user.UserVoucherRecord;
import com.balance.exception.WeChatPayNotifyException;
import com.balance.mapper.shop.GoodsSpuMapper;
import com.balance.mapper.shop.OrderMapper;
import com.balance.service.common.GlobalConfigService;
import com.balance.service.common.WeChatPayService;
import com.balance.service.user.UserAssetsService;
import com.balance.service.user.UserMemberService;
import com.balance.utils.ValueCheckUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class OrderNotifyService extends BaseService {

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private WeChatPayService weChatPayService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private GoodsSpuMapper goodsSpuMapper;

    @Autowired
    private UserMemberService userMemberService;

    @Autowired
    private UserAssetsService userAssetsService;

    @Autowired
    private GlobalConfigService globalConfigService;

    @Autowired
    private OrderService orderService;

    /**
     * 线上领取小样微信支付通知
     *
     * @param request
     * @throws IOException
     */
    public String receiveBeautyWeChatPayNotify(HttpServletRequest request) throws IOException {
        WeChatPayNotifyRecord weChatPayNotifyRecord = weChatPayService.notifyAnalyse(request.getInputStream());
        OrderInfo orderInfo = selectOneById(weChatPayNotifyRecord.getMchOrderId(), OrderInfo.class);
        if (orderInfo == null) {
            throw new WeChatPayNotifyException("未找到订单信息");
        }
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                String userId = orderInfo.getUserId();
                orderMapper.updateOrderHadPay(orderInfo.getId(), userId);
                insert(weChatPayNotifyRecord);
                User user = selectOneById(userId, User.class);
                userAssetsService.updateRMBAssetsByTurnoverType(user, AssetTurnoverConst.TURNOVER_TYPE_BEAUTY_RECEIVE, "被邀请用户线上领取美妆样品服务费");

                orderService.payOrder(orderInfo.getId(), ShopConst.ORDER_STATUS_DELIVERY);
            }
        });

        return ResultUtils.weChatPaySuccess();

    }

    /**
     * 办理年卡微信支付通知
     *
     * @param request
     */
    public String becomeMemberWeChatPayNotify(HttpServletRequest request) throws IOException {
        WeChatPayNotifyRecord weChatPayNotifyRecord = weChatPayService.notifyAnalyse(request.getInputStream());
        OrderInfo orderInfo = selectOneById(weChatPayNotifyRecord.getMchOrderId(), OrderInfo.class);
        if (orderInfo == null) {
            throw new WeChatPayNotifyException("未找到订单信息");
        }
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {

                String userId = orderInfo.getUserId();
                orderMapper.updateOrderHadPay(orderInfo.getId(), orderInfo.getUserId());
                insert(weChatPayNotifyRecord);

                userMemberService.becomeMember(userId, UserConst.USER_MEMBER_TYPE_COMMON);

                orderService.payOrder(orderInfo.getId(), ShopConst.ORDER_STATUS_RECEIVE);
            }
        });

        return ResultUtils.weChatPaySuccess();
    }


    /**
     * 购买小样礼包卡券微信支付回调
     *
     * @param request
     * @return
     * @throws IOException
     */
    public String buyBeautyVoucherWeChatPayNotify(HttpServletRequest request) throws IOException {
        WeChatPayNotifyRecord weChatPayNotifyRecord = weChatPayService.notifyAnalyse(request.getInputStream());
        OrderInfo orderInfo = selectOneById(weChatPayNotifyRecord.getMchOrderId(), OrderInfo.class);
        if (orderInfo == null) {
            throw new WeChatPayNotifyException("未找到订单信息");
        }
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                String userId = orderInfo.getUserId();
                orderMapper.updateOrderHadPay(orderInfo.getId(), orderInfo.getUserId());
                insert(weChatPayNotifyRecord);

                ShopVoucher shopVoucher = selectOneByWhereString(ShopVoucher.Voucher_type + "=", ShopConst.VOUCHER_TYPE_BEAUTY_PACKAGE_DEDUCTION, ShopVoucher.class);
                Integer voucherAliveDayTimes = Integer.valueOf(globalConfigService.get(GlobalConfigService.Enum.BUYING_VOUCHER_ALIVE_DAY_TIMES));
                Timestamp unableTime = new Timestamp(DateUtils.addDays(new Date(), voucherAliveDayTimes).getTime());
                UserVoucherRecord userVoucherRecord = new UserVoucherRecord(userId, 1, shopVoucher.getId(), unableTime);
                if (insertIfNotNull(userVoucherRecord) == 0) {
                    SystemExceptionRecord systemExceptionRecord = new SystemExceptionRecord("用户id为：" + userId + "购买了小样礼包卡券，已成功支付，但系统新增卡券记录失败");
                    insertIfNotNull(systemExceptionRecord);
                }

                orderService.payOrder(orderInfo.getId(), ShopConst.ORDER_STATUS_RECEIVE);
            }
        });

        return ResultUtils.weChatPaySuccess();
    }
}
