package com.balance.service.shop;

import com.alibaba.fastjson.JSONObject;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.constance.AssetTurnoverConst;
import com.balance.controller.app.req.OrderSkuReq;
import com.balance.entity.shop.*;
import com.balance.entity.user.UserAssets;
import com.balance.mapper.shop.OrderMapper;
import com.balance.service.user.AssetsTurnoverService;
import com.balance.service.user.UserAssetsService;
import com.balance.utils.BigDecimallUtils;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
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
     * @param userId          用户Id
     * @param addressId       地址id
     * @param settlementId    支付id
     * @throws BusinessException
     */
    public void createOrder(List<OrderSkuReq> orderSkuReqList, String userId, String addressId, Integer settlementId) throws BusinessException {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                BigDecimal orderTotalPrice = BigDecimallUtils.newObject(0d);
                List<OrderItem> orderItems = new ArrayList<>();
                for (OrderSkuReq orderSkuReq : orderSkuReqList) {
                    //1.检查商品是否支持用户选择的支付方式
                    BigDecimal skuNumber = BigDecimallUtils.newObject(orderSkuReq.getNumber());
                    String spuId = orderSkuReq.getSpuId();
                    Map<String, Object> spuWhereMap = ImmutableMap.of("spu_id = ", spuId, "settlement_id = ", settlementId);
                    GoodsSpu goodsSpu = selectOneByWhereMap(spuWhereMap, GoodsSpu.class);
                    if (goodsSpu == null) {
                        throw new BusinessException("商品不支持该支付方式");
                    }

                    //sku字符串转换
                    List<String> specNameValueStrList = orderSkuReq.getSpecNameValueStrList();
                    Map<String, String> skuSpecMap = new HashMap<>(); //key:规格名id， value:规格值id
                    for (String specNameValueStr : specNameValueStrList) { //specNameValueStr格式 = "规格名id:规格值id"
                        String[] specArr = specNameValueStr.split(":");
                        skuSpecMap.put(specArr[0], specArr[1]);
                    }

                    //2.计算订单总价格
                    Map<String, Object> skuWhereMap = ImmutableMap.of("spu_id = ", spuId, "spec_json = ", JSONObject.toJSONString(skuSpecMap));
                    GoodsSku goodsSku = selectOneByWhereMap(skuWhereMap, GoodsSku.class);
                    if (goodsSku == null) {
                        throw new BusinessException("未找到商品");
                    }

                    if(orderSkuReq.getNumber()>goodsSku.getStock()){
                        throw new BusinessException("商品编号"+goodsSku.getSkuNo()+"库存不足");
                    }

                    BigDecimal spuTotalPrice = BigDecimallUtils.multiply(goodsSku.getPrice(), skuNumber);
                    orderTotalPrice = BigDecimallUtils.add(orderTotalPrice, spuTotalPrice);

                    orderItems.add(new OrderItem(spuId, goodsSku.getId(), orderSkuReq.getNumber(), goodsSku.getPrice(), spuTotalPrice));
                }

                //3.增加订单记录
                String orderNumber = DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmssSS");
                OrderInfo orderInfo = new OrderInfo(orderNumber, settlementId, userId, addressId, orderTotalPrice);
                insertIfNotNull(orderInfo);
                for (OrderItem orderItem : orderItems) {
                    orderItem.setOrderId(orderInfo.getId());
                }
                insertBatch(orderItems, false);

                //4.扣除用户资产
                UserAssets userAssets = selectOneByWhereString("user_id = ", userId, UserAssets.class);

                Integer i = userAssetsService.changeUserAssets(userId, orderTotalPrice, settlementId, userAssets);
                if (i == 0) {
                    throw new BusinessException("用户资产不足");
                }

                //5.增加流水记录
                String detailStr = "商城购物支付,订单号为:" + orderNumber;
                assetsTurnoverService.createAssetsTurnover(userId, AssetTurnoverConst.TURNOVER_TYPE_SHOPPING_ORDER_PAY, orderTotalPrice, userId, "0", userAssets, settlementId, detailStr);
            }
        });
    }

    /**
     * 用户的订单列表
     * @param userId 用户id
     * @param orderStatus 订单状态
     * @return
     */
    public List<OrderGoodsInfo> listOrderGoodsInfo(String userId, Integer orderStatus) {
        List<OrderGoodsInfo> orderGoodsInfoList = orderMapper.listOrderGoodsInfo(userId, orderStatus);

        for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {//订单列表
            for (OrderItem orderItem : orderGoodsInfo.getOrderItemList()) {//订单商品列表
                orderItem.setSpecStr(goodsSpecService.buildOrderItemSpecStr(orderItem.getSpecJson()));
            }
        }
        return orderGoodsInfoList;
    }

    /**
     * 用户订单详情
     * @param orderId
     * @return
     */
    public OrderGoodsInfo getOrderGoodsInfo(String orderId){
        OrderGoodsInfo orderGoodsInfo = orderMapper.getOrderGoodsInfo(orderId);
        for (OrderItem orderItem : orderGoodsInfo.getOrderItemList()) {//订单商品列表
            orderItem.setSpecStr(goodsSpecService.buildOrderItemSpecStr(orderItem.getSpecJson()));
        }
        return orderGoodsInfo;
    }

}
