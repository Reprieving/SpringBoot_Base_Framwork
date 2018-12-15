package com.balance.service.shop;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.architecture.utils.ValueCheckUtils;
import com.balance.constance.AssetTurnoverConst;
import com.balance.controller.app.req.ShopOrderSkuReq;
import com.balance.entity.shop.*;
import com.balance.entity.user.User;
import com.balance.entity.user.UserAssets;
import com.balance.mapper.shop.OrderMapper;
import com.balance.service.user.AssetsTurnoverService;
import com.balance.service.user.UserAssetsService;
import com.balance.utils.BigDecimalUtils;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService extends BaseService {

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private UserAssetsService userAssetsService;

    @Autowired
    private AssetsTurnoverService assetsTurnoverService;

    @Autowired
    private GoodsSpecService goodsSpecService;

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 商城下单
     *
     * @param orderSkuReqList
     * @param user            用户
     * @param addressId       地址id
     * @param settlementId    支付id
     * @throws BusinessException
     */
    public void createOrder(List<ShopOrderSkuReq> orderSkuReqList, User user, String addressId, Integer settlementId) throws BusinessException {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                ValueCheckUtils.notEmpty(user, "未找到用户");
                String userId = user.getId();
                BigDecimal orderTotalPrice = BigDecimalUtils.newObject(0d);

                Map<String, List<OrderItem>> shopOrderItemMap = new HashMap<>(); // 商铺Id对应的订单

                for (ShopOrderSkuReq orderSkuReq : orderSkuReqList) {
                    //1.检查商品是否支持用户选择的支付方式
                    BigDecimal skuNumber = BigDecimalUtils.newObject(orderSkuReq.getNumber());
                    String spuId = orderSkuReq.getSpuId();
                    Map<String, Object> spuWhereMap = ImmutableMap.of(GoodsSpu.Id + " = ", spuId, GoodsSpu.Settlement_id + " = ", settlementId);
                    GoodsSpu goodsSpu = selectOneByWhereMap(spuWhereMap, GoodsSpu.class);
                    ValueCheckUtils.notEmpty(goodsSpu, "商品不支持该支付方式");

                    //2.计算订单总价格
                    Map<String, Object> skuWhereMap = ImmutableMap.of(GoodsSku.Spu_id + " = ", spuId, GoodsSku.Spec_json + " = ", orderSkuReq.getSpecIdStr());
                    GoodsSku goodsSku = selectOneByWhereMap(skuWhereMap, GoodsSku.class);
                    ValueCheckUtils.notEmpty(goodsSku, "未找到商品");

                    if (orderSkuReq.getNumber() > goodsSku.getStock()) {
                        throw new BusinessException("商品编号" + goodsSku.getSkuNo() + "库存不足");
                    }

                    BigDecimal spuTotalPrice = BigDecimalUtils.multiply(goodsSku.getPrice(), skuNumber);
                    orderTotalPrice = BigDecimalUtils.add(orderTotalPrice, spuTotalPrice);

                    String shopId = goodsSpu.getShopId();
                    ShopInfo shopInfo = selectOneById(goodsSpu.getShopId(), ShopInfo.class);
                    ValueCheckUtils.notEmpty(shopInfo, "未找到商铺信息");

                    //如果shopOrderItemMap包含商铺id 获取value插入新的orderItem,
                    List<OrderItem> orderItemsList;
                    if (shopOrderItemMap.containsKey(shopId)) {
                        orderItemsList = shopOrderItemMap.get(shopId);
                        orderItemsList.add(new OrderItem(spuId, goodsSku.getId(), orderSkuReq.getNumber(), goodsSku.getPrice(), spuTotalPrice));
                    } else {
                        orderItemsList = new ArrayList<>();
                        orderItemsList.add(new OrderItem(spuId, goodsSku.getId(), orderSkuReq.getNumber(), goodsSku.getPrice(), spuTotalPrice));
                        shopOrderItemMap.put(shopId, orderItemsList);
                    }
                }

                //3.增加订单记录
                for (Map.Entry<String, List<OrderItem>> entry : shopOrderItemMap.entrySet()) {
                    //订单信息
                    String shopId = entry.getKey();
                    String orderNumber = DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmssSSS");
                    OrderInfo orderInfo = new OrderInfo(orderNumber, settlementId, userId, shopId, user.getUserName(), addressId, orderTotalPrice);
                    insertIfNotNull(orderInfo);

                    //订单详情
                    List<OrderItem> orderItemList = entry.getValue();
                    for (OrderItem orderItem : orderItemList) {
                        orderItem.setOrderId(orderInfo.getId());
                    }
                    insertBatch(orderItemList, false);

                    //4.扣除用户资产
                    UserAssets userAssets = selectOneByWhereString(UserAssets.User_id + " = ", userId, UserAssets.class);

                    BigDecimal assets = userAssetsService.getAssetsBySettlementId(userAssets, settlementId);
                    int a = assets.compareTo(orderTotalPrice);
                    if (a == -1) {
                        throw new BusinessException("用户资产不足");
                    }

                    Integer i = userAssetsService.changeUserAssets(userId, orderTotalPrice, settlementId, userAssets);
                    if (i == 0) {
                        throw new BusinessException("支付失败");
                    }

                    //5.增加流水记录
                    String detailStr = "商城购物支付,订单号为:" + orderNumber;
                    assetsTurnoverService.createAssetsTurnover(userId, AssetTurnoverConst.TURNOVER_TYPE_SHOPPING_ORDER_PAY, orderTotalPrice, userId, AssetTurnoverConst.COMPANY_ID, userAssets, settlementId, detailStr);
                }
            }
        });
    }

    /**
     * APP用户的订单列表
     *
     * @param userId      用户id
     * @param orderStatus 订单状态
     * @param pagination
     * @return
     */
    public List<OrderGoodsInfo> listAppOrderGoodsInfo(String userId, Integer orderStatus, Pagination pagination) {
        List<OrderGoodsInfo> orderGoodsInfoList = orderMapper.listUserOrderGoodsInfo(userId, orderStatus, pagination);
        for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {//订单列表
            for (OrderItem orderItem : orderGoodsInfo.getOrderItemList()) {//订单商品列表
                orderItem.setSpecStr(goodsSpecService.strSpecIdToSpecValue(orderItem.getSpecJson()));
            }
        }
        return orderGoodsInfoList;
    }

    /**
     * APP用户订单详情
     *
     * @param orderId
     * @return
     */
    public OrderGoodsInfo getAppOrderGoodsInfo(String orderId) {
        OrderGoodsInfo orderGoodsInfo = orderMapper.getUserOrderGoodsInfo(orderId);
        for (OrderItem orderItem : orderGoodsInfo.getOrderItemList()) {//订单商品列表
            orderItem.setSpecStr(goodsSpecService.strSpecIdToSpecValue(orderItem.getSpecJson()));
        }
        return orderGoodsInfo;
    }


    /**
     * 后台用户订单列表
     *
     * @param orderQueryInfo 订单条件查询实体
     * @param pagination     分页实体
     * @return
     */
    public List<OrderGoodsInfo> listAdminOrderGoodsInfo(OrderInfo orderQueryInfo, Pagination pagination) {
        List<OrderGoodsInfo> orderGoodsInfoList = orderMapper.listAdminOrderGoodsInfo(
                orderQueryInfo.getOrderNo(), orderQueryInfo.getUserName(), orderQueryInfo.getSettlementId(), orderQueryInfo.getLogisticNumber(),
                orderQueryInfo.getStartTime(), orderQueryInfo.getEndTime(), orderQueryInfo.getStatus(), pagination
        );
        for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {//订单列表
            for (OrderItem orderItem : orderGoodsInfo.getOrderItemList()) {//订单商品列表
                orderItem.setSpecStr(goodsSpecService.strSpecIdToSpecValue(orderItem.getSpecJson()));
            }
        }
        return orderGoodsInfoList;
    }
}
