package com.balance.service.shop;

import com.alibaba.fastjson.JSONObject;
import com.balance.architecture.dto.Pagination;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.constance.*;
import com.balance.entity.mission.Mission;
import com.balance.entity.user.*;
import com.balance.service.user.UserMerchantService;
import com.balance.utils.ValueCheckUtils;
import com.balance.controller.app.req.ShopOrderSkuReq;
import com.balance.entity.common.CodeEntity;
import com.balance.entity.shop.*;
import com.balance.mapper.shop.OrderMapper;
import com.balance.mapper.user.UserMapper;
import com.balance.mapper.user.UserVoucherMapper;
import com.balance.service.mission.MissionService;
import com.balance.service.user.AssetsTurnoverService;
import com.balance.service.user.UserAssetsService;
import com.balance.utils.BigDecimalUtils;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private UserAssetsService userAssetsService;

    @Autowired
    private AssetsTurnoverService assetsTurnoverService;

    @Autowired
    private GoodsSpecService goodsSpecService;

    @Autowired
    private ShopVoucherService shopVoucherService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserVoucherMapper userVoucherMapper;

    @Autowired
    private MissionService missionService;

    @Autowired
    private UserMerchantService userMerchantService;

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
                Map<String, List<OrderItem>> shopOrderItemMap = new HashMap<>(); // 商铺Id对应的订单
                for (ShopOrderSkuReq orderSkuReq : orderSkuReqList) {
                    //1.检查商品是否支持用户选择的支付方式
                    BigDecimal skuNumber = BigDecimalUtils.newObject(orderSkuReq.getNumber());
                    String spuId = orderSkuReq.getSpuId();
                    Map<String, Object> spuWhereMap = ImmutableMap.of(GoodsSpu.Id + " = ", spuId, GoodsSpu.Settlement_id + " = ", settlementId);
                    GoodsSpu goodsSpu = selectOneByWhereMap(spuWhereMap, GoodsSpu.class);
                    ValueCheckUtils.notEmpty(goodsSpu, "商品不支持该支付方式");

                    //2.计算订单总价格
                    String skuOrSpuId;
                    BigDecimal skuOrSpuPrice;

                    if (StringUtils.isNotBlank(orderSkuReq.getSpecIdStr())) {//有sku的商品计算价格
                        Map<String, Object> skuWhereMap = ImmutableMap.of(GoodsSku.Spu_id + " = ", spuId, GoodsSku.Spec_json + " = ", orderSkuReq.getSpecIdStr());
                        GoodsSku goodsSku = selectOneByWhereMap(skuWhereMap, GoodsSku.class);
                        ValueCheckUtils.notEmpty(goodsSku, "未找到商品");
                        if (orderSkuReq.getNumber() > goodsSku.getStock()) {
                            throw new BusinessException("商品编号" + goodsSku.getSkuNo() + "库存不足");
                        }
                        skuOrSpuId = goodsSku.getId();
                        skuOrSpuPrice = goodsSku.getPrice();
                    } else {//没有sku的商品计算价格
                        skuOrSpuId = spuId;
                        skuOrSpuPrice = goodsSpu.getLowPrice();
                    }

                    BigDecimal freight = goodsSpu.getFreight();
                    if (new BigDecimal(1).compareTo(freight) == -1 && freight.compareTo(new BigDecimal(0)) == 1) {
                        throw new BusinessException("邮费异常");
                    }

                    BigDecimal spuTotalPrice = BigDecimalUtils.multiply(skuOrSpuPrice, skuNumber);
                    //如果邮费等于0，则包邮
                    if (BigDecimalUtils.ifZero(freight)) {
                        spuTotalPrice = BigDecimalUtils.multiply(spuTotalPrice, freight);
                    }

                    //获取商铺信息
                    String shopId = goodsSpu.getShopId();
                    ShopInfo shopInfo = selectOneById(goodsSpu.getShopId(), ShopInfo.class);
                    ValueCheckUtils.notEmpty(shopInfo, "未找到商铺信息");

                    //如果shopOrderItemMap包含商铺id 获取value插入新的orderItem,
                    List<OrderItem> orderItemsList;
                    if (shopOrderItemMap.containsKey(shopId)) {
                        orderItemsList = shopOrderItemMap.get(shopId);
                        orderItemsList.add(new OrderItem(spuId, skuOrSpuId, orderSkuReq.getNumber(), skuOrSpuPrice, spuTotalPrice, freight));
                    } else {
                        orderItemsList = new ArrayList<>();
                        orderItemsList.add(new OrderItem(spuId, skuOrSpuId, orderSkuReq.getNumber(), skuOrSpuPrice, spuTotalPrice, freight));
                        shopOrderItemMap.put(shopId, orderItemsList);
                    }
                }

                //3.增加订单记录
                for (Map.Entry<String, List<OrderItem>> entry : shopOrderItemMap.entrySet()) {
                    BigDecimal orderTotalPrice = BigDecimalUtils.newObject(0); // 订单总价格
                    BigDecimal orderTotalFreight = BigDecimalUtils.newObject(0); // 订单总邮费
                    String shopId = entry.getKey(); //商铺id
                    List<OrderItem> orderItemList = entry.getValue();  //订单详情
                    //获取订单总价格
                    for (OrderItem orderItem : orderItemList) {
                        orderTotalPrice = BigDecimalUtils.add(orderTotalPrice, orderItem.getTotalPrice());
                        orderTotalFreight = BigDecimalUtils.add(orderTotalFreight, orderItem.getFreight());
                    }
                    String orderNumber = DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmssSSS");
                    OrderInfo orderInfo = new OrderInfo(orderNumber, settlementId, userId, shopId, user.getUserName(), addressId, orderTotalPrice, orderTotalFreight);
                    insertIfNotNull(orderInfo);

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
     * 扫码领取小样
     *
     * @param userId      用户id
     * @param aisleCode   小样编码
     * @param machineCode 小样机器编码
     */
    public void scanBeauty(String userId, String aisleCode, String machineCode) {
        User user = selectOneById(userId, User.class);
        if (StringUtils.isBlank(user.getWxOpenId())) {
            throw new BusinessException("请绑定微信号后再扫码领取");
        }
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                //扫码接口
                PostMethod post = null;
                try {
                    HttpClient client = new HttpClient();
                    post = new PostMethod("http://pinkjewelry.cn/pinkjewelry/payment/freeCollenctionGoods");
                    post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");//在头文件中设置转码
                    NameValuePair[] data = {new NameValuePair("openid", user.getWxOpenId()), new NameValuePair("aisle_code", aisleCode), new NameValuePair("machine_code", machineCode)};
                    post.setRequestBody(data);
                    client.executeMethod(post);
                    String result = new String(post.getResponseBodyAsString().getBytes("utf-8"));

                    CodeEntity codeEntity = JSONObject.parseObject(result, CodeEntity.class);
                    if (codeEntity != null && codeEntity.getCode() != "00") {
                        throw new BusinessException("库存异常");
                    }
                    //完成任务，并增加颜值
                    missionService.finishMission(user, MissionConst.OBTAIN_BEAUTY, "APP线上领取小样");
                } catch (IOException e) {
                    logger.error(e.getMessage());
                } finally {
                    if (post != null) {
                        post.releaseConnection();
                    }
                }

                if (user.getType() == UserConst.USER_MERCHANT_TYPE_BEING) {
                    //查询用户商户签约记录
                    Map<String, Object> whereMap = ImmutableMap.of(UserMerchantRecord.User_id + "=", userId, UserMerchantRecord.If_valid + "=", true);
                    UserMerchantRecord userMerchantRecord = selectOneByWhereMap(whereMap, UserMerchantRecord.class);

                    if (userMerchantRecord != null) {
                        Mission mission = missionService.filterTaskByCode(MissionConst.OBTAIN_BEAUTY, missionService.allMissionList()); //颜值任务奖励
                        UserMerchantRuler userMerchantRuler = userMerchantService.filterMerchantRulerById(userMerchantRecord.getMerchantRulerId());
                        if (userMerchantRuler != null) {
                            String inviteId = user.getInviteId();
                            UserAssets userAssets = userAssetsService.getAssetsByUserId(inviteId);

                            //颜值分润值
                            BigDecimal computeShareProfit = BigDecimalUtils.multiply(
                                    missionService.getMissionRewardByMemberType(user.getMemberType(), mission),
                                    BigDecimalUtils.percentToRate(userMerchantRuler.getComputeReturnRate()
                                    ));
                            //增加邀请用户颜值
                            Integer computeSettlementId = mission.getSettlementId();
                            userAssetsService.changeUserAssets(inviteId, computeShareProfit, computeSettlementId, userAssets);
                            assetsTurnoverService.createAssetsTurnover(
                                    inviteId, AssetTurnoverConst.TURNOVER_TYPE_COMPUTE_RETURN, computeShareProfit, AssetTurnoverConst.COMPANY_ID,
                                    inviteId, userAssets, computeSettlementId, "被邀请用户线上领取美妆样品颜值返现分利"
                            );

                            //样品红利
                            BigDecimal beautyServiceShareProfit = userMerchantRuler.getBeautyServiceProfit();
                            Integer rmbSettlementId = SettlementConst.SETTLEMENT_RMB;
                            //增加邀请用户人民币
                            userAssetsService.changeUserAssets(inviteId, beautyServiceShareProfit, rmbSettlementId, userAssets);
                            assetsTurnoverService.createAssetsTurnover(
                                    inviteId, AssetTurnoverConst.TURNOVER_TYPE_BEAUTY_OBTAIN, beautyServiceShareProfit, AssetTurnoverConst.COMPANY_ID,
                                    inviteId, userAssets, computeSettlementId, "被邀请用户线上领取美妆样品服务费"
                            );
                        }

                    } else {
                        logger.error("用户id为：" + userId + "的用户类型异常，该用户现用户类型为商户，但在商户签约表中无有效记录");
                    }
                }

                //获取小样信息接口,创建订单

            }
        });

    }


    /**
     * 卡券兑换礼包
     *
     * @param userId 用户id
     * @param spuId  商品id
     */
    public void exchangeSpuPackage(String userId, String voucherId, String spuId, String addressId) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                UserVoucherRecord userVoucherRecord = userVoucherMapper.getUserVoucher(userId, voucherId);
                if (userVoucherRecord.getQuantity() == 0) {
                    throw new BusinessException("卡券已用光");
                }

                if (shopVoucherService.checkIfPackageVoucher(userVoucherRecord.getVoucherType())) {
                    //扣除用户优惠券数量
                    if (userVoucherMapper.decreaseQuantity(userVoucherRecord.getId(), userVoucherRecord.getVersion()) == 0) {
                        throw new BusinessException("兑换失败");
                    }

                    //创建订单
                    ShopInfo officialShop = selectOneById(ShopConst.SHOP_OFFICIAL, ShopInfo.class);
                    ValueCheckUtils.notEmpty(officialShop, "未找到商铺");
                    GoodsSpu goodsSpu = selectOneById(spuId, GoodsSpu.class);
                    ValueCheckUtils.notEmpty(goodsSpu, "未找到商品");

                    User user = selectOneById(userId, User.class);
                    String shopId = officialShop.getId();
                    Integer settlementId = SettlementConst.SETTLEMENT_VOUCHER;
                    BigDecimal orderTotalPrice = goodsSpu.getLowPrice();


                    String orderNumber = DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmssSSS");
                    OrderInfo orderInfo = new OrderInfo(orderNumber, settlementId, userId, shopId, user.getUserName(), addressId, new BigDecimal(0), orderTotalPrice);

                    if (insertIfNotNull(orderInfo) == 0) {
                        throw new BusinessException("兑换失败");
                    }

                    missionService.finishMission(user, MissionConst.EXCHANGE_PACKAGE, "兑换礼包");

                    if (user.getType() == UserConst.USER_MERCHANT_TYPE_BEING) {
                        //查询用户商户签约记录
                        Map<String, Object> whereMap = ImmutableMap.of(UserMerchantRecord.User_id + "=", userId, UserMerchantRecord.If_valid + "=", true);
                        UserMerchantRecord userMerchantRecord = selectOneByWhereMap(whereMap, UserMerchantRecord.class);
                        if (userMerchantRecord != null) {
                            Mission mission = missionService.filterTaskByCode(MissionConst.EXCHANGE_PACKAGE, missionService.allMissionList()); //兑换礼包奖励值
                            UserMerchantRuler userMerchantRuler = userMerchantService.filterMerchantRulerById(userMerchantRecord.getMerchantRulerId());
                            if (userMerchantRuler != null) {
                                String inviteId = user.getInviteId();
                                UserAssets userAssets = userAssetsService.getAssetsByUserId(inviteId);

                                //颜值分润值
                                BigDecimal computeShareProfit = BigDecimalUtils.multiply(
                                        missionService.getMissionRewardByMemberType(user.getMemberType(), mission),
                                        BigDecimalUtils.percentToRate(userMerchantRuler.getComputeReturnRate()
                                        ));

                                //增加邀请用户颜值
                                Integer computeSettlementId = mission.getSettlementId();
                                userAssetsService.changeUserAssets(inviteId, computeShareProfit, computeSettlementId, userAssets);
                                assetsTurnoverService.createAssetsTurnover(
                                        inviteId, AssetTurnoverConst.TURNOVER_TYPE_COMPUTE_RETURN, computeShareProfit, AssetTurnoverConst.COMPANY_ID,
                                        inviteId, userAssets, computeSettlementId, "被邀请用户线上兑换礼包颜值返现分利"
                                );
                            }

                        }else {
                            logger.error("用户id为：" + userId + "的用户类型异常，该用户现用户类型为商户，但在商户签约表中无有效记录");
                        }
                    }

                } else {
                    throw new BusinessException("暂只支持年卡会员卡券，生日卡券使用");
                }
            }
        });
    }


    /**
     * APP用户的订单列表
     *
     * @param userId      用户id
     * @param orderStatus 订单状态
     * @param ifScan      是否扫码领取小样的订单
     * @param pagination
     * @return
     */
    public List<OrderGoodsInfo> listAppOrderGoodsInfo(String userId, Integer orderStatus, Boolean ifScan, Pagination pagination) {
        List<OrderGoodsInfo> orderGoodsInfoList;
        if (ifScan != null && ifScan == true) {
            orderGoodsInfoList = orderMapper.listUserBeautyGoodsInfo(userId, ifScan);
        } else {
            orderGoodsInfoList = orderMapper.listUserOrderGoodsInfo(userId, orderStatus, pagination);
            for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {//订单列表
                for (OrderItem orderItem : orderGoodsInfo.getOrderItemList()) {//订单商品列表
                    orderItem.setSpecStr(goodsSpecService.strSpecIdToSpecValue(orderItem.getSpecJson()));
                }
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
        ShoppingAddress shoppingAddress = selectOneById(orderGoodsInfo.getAddressId(), ShoppingAddress.class);
        for (OrderItem orderItem : orderGoodsInfo.getOrderItemList()) {//订单商品列表
            orderItem.setSpecStr(goodsSpecService.strSpecIdToSpecValue(orderItem.getSpecJson()));
        }

        orderGoodsInfo.setShoperName(shoppingAddress.getShoperName());
        orderGoodsInfo.setPhoneNumber(shoppingAddress.getShoperTel());
        orderGoodsInfo.setDetailAddress(shoppingAddress.getShoperProvince() + shoppingAddress.getShoperCity() + shoppingAddress.getShoperDistrict() + shoppingAddress.getShoperAddress());

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
