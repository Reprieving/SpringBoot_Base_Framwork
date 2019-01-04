package com.balance.service.shop;

import com.balance.architecture.service.BaseService;
import com.balance.architecture.utils.ResultUtils;
import com.balance.constance.AssetTurnoverConst;
import com.balance.constance.UserConst;
import com.balance.entity.common.WeChatPayNotifyRecord;
import com.balance.entity.shop.OrderInfo;
import com.balance.entity.shop.OrderItem;
import com.balance.entity.user.User;
import com.balance.exception.WeChatPayNotifyException;
import com.balance.mapper.shop.GoodsSpuMapper;
import com.balance.mapper.shop.OrderMapper;
import com.balance.service.common.WeChatPayService;
import com.balance.service.user.UserAssetsService;
import com.balance.service.user.UserMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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

                List<OrderItem> orderItemList = selectListByWhereString(OrderItem.Order_id + "=", orderInfo.getId(), null, OrderItem.class);

                orderItemList.forEach(orderItem -> goodsSpuMapper.decreaseSpuStock(orderItem.getSpuId(), 1));
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
            }
        });

        return ResultUtils.weChatPaySuccess();
    }
}
