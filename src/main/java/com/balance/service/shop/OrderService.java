package com.balance.service.shop;

import com.alibaba.fastjson.JSONObject;
import com.balance.architecture.dto.Pagination;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.client.RedisClient;
import com.balance.constance.*;
import com.balance.controller.app.req.ShopOrderPayReq;
import com.balance.entity.mission.Mission;
import com.balance.entity.mission.MissionReward;
import com.balance.entity.user.*;
import com.balance.exception.UserVoucherNoneException;
import com.balance.mapper.shop.GoodsSkuMapper;
import com.balance.mapper.shop.GoodsSpuMapper;
import com.balance.service.common.GlobalConfigService;
import com.balance.service.common.WeChatPayService;
import com.balance.service.user.UserMerchantService;
import com.balance.utils.*;
import com.balance.controller.app.req.ShopOrderSkuReq;
import com.balance.entity.common.CodeEntity;
import com.balance.entity.shop.*;
import com.balance.mapper.shop.OrderMapper;
import com.balance.mapper.user.UserVoucherMapper;
import com.balance.service.mission.MissionService;
import com.balance.service.user.AssetsTurnoverService;
import com.balance.service.user.UserAssetsService;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

@Service
public class OrderService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private GlobalConfigService globalConfigService;

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
    private UserVoucherMapper userVoucherMapper;

    @Autowired
    private MissionService missionService;

    @Autowired
    private UserMerchantService userMerchantService;

    @Autowired
    private WeChatPayService weChatPayService;

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private GoodsSpuMapper goodsSpuMapper;

    @Autowired
    private GoodsSkuMapper goodsSkuMapper;

    /**
     * 创建订单
     *
     * @param shopOrderPayReq 下单信息
     * @param request         请求体
     */
    public Map<String, String> payOrder(User user, ShopOrderPayReq shopOrderPayReq, HttpServletRequest request) {
        Integer orderType = shopOrderPayReq.getOrderType();
        ValueCheckUtils.notEmpty(orderType, "订单类型异常");
        switch (orderType) {
            case ShopConst.ORDER_TYPE_SHOPPING: //商城代币购物
                return shoppingOrder(shopOrderPayReq.getOrderSkuReqList(), user, shopOrderPayReq.getAddressId(), shopOrderPayReq.getSettlementId());

            case ShopConst.ORDER_TYPE_RECEIVE_BEAUTY://线上领取小样
                return receiveBeautyOrder(user, shopOrderPayReq.getSpuId(), shopOrderPayReq.getAddressId(), shopOrderPayReq.getSettlementId(), request);

            case ShopConst.ORDER_TYPE_SCAN_BEAUTY://线下扫码领取小样
                return scanBeautyOrder(user.getId(), shopOrderPayReq.getQrCodeStr());

            case ShopConst.ORDER_TYPE_EXCHANGE_BEAUTY://线上兑换小样礼包
                return exchangeSpuPackageOrder(user.getId(), shopOrderPayReq.getVoucherId(), shopOrderPayReq.getSpuId(), shopOrderPayReq.getAddressId());

            case ShopConst.ORDER_TYPE_BECOME_MEMBER://办理年卡会员
                return becomeMemberOrder(user.getId(), shopOrderPayReq.getSettlementId(), request);

            default:
                throw new BusinessException("数据异常");
        }

    }


    /**
     * 商城下单(限用于APP代币支付)
     *
     * @param orderSkuReqList
     * @param user            用户
     * @param addressId       地址id
     * @param settlementId    支付id
     * @throws BusinessException
     */
    public Map<String, String> shoppingOrder(List<ShopOrderSkuReq> orderSkuReqList, User user, String addressId, Integer settlementId) throws BusinessException {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                ShoppingAddress shoppingAddress = selectOneById(addressId, ShoppingAddress.class);
                ValueCheckUtils.notEmpty(shoppingAddress, "未找到收货地址记录");

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
                    Boolean ifSku = goodsSpu.getIfSku();


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
                        orderItemsList.add(new OrderItem(spuId, skuOrSpuId, orderSkuReq.getNumber(), skuOrSpuPrice, spuTotalPrice, freight, ifSku));
                    } else {
                        orderItemsList = new ArrayList<>();
                        orderItemsList.add(new OrderItem(spuId, skuOrSpuId, orderSkuReq.getNumber(), skuOrSpuPrice, spuTotalPrice, freight, ifSku));
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
                        orderTotalPrice = BigDecimalUtils.add(orderTotalPrice, orderTotalFreight);
                    }

                    Timestamp createTime = new Timestamp(System.currentTimeMillis());
                    String orderNumber = OrderNoUtils.buildOrderNo();
                    OrderInfo orderInfo = new OrderInfo(
                            orderNumber, settlementId, userId, shopId, user.getUserName(), addressId, orderTotalPrice, orderTotalFreight, ShopConst.ORDER_TYPE_SHOPPING,
                            ShopConst.ORDER_STATUS_NONE, createTime
                    );
                    insertIfNotNull(orderInfo);

                    for (OrderItem orderItem : orderItemList) {
                        orderItem.setOrderId(orderInfo.getId());
                        //扣库存
                        Integer i;
                        if (orderItem.getIfSku()) {//扣sku库存
                            i = goodsSkuMapper.decreaseSkuStock(orderItem.getGoodsSkuId(), orderItem.getNumber());
                        } else {//扣spu库存
                            i = goodsSpuMapper.decreaseSpuStock(orderItem.getSpuId(), orderItem.getNumber());
                        }
                        if (i == 0) {
                            throw new BusinessException("商品库存不足");
                        }
                    }
                    insertBatch(orderItemList, false);

                    //扣除用户资产
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

                    //增加流水记录
                    String detailStr = "商城购物支付,订单号为:" + orderNumber;
                    assetsTurnoverService.createAssetsTurnover(
                            userId, AssetTurnoverConst.TURNOVER_TYPE_SHOPPING_ORDER_PAY, orderTotalPrice, userId, AssetTurnoverConst.COMPANY_ID, userAssets, settlementId, detailStr
                    );
                }
            }
        });
        return null;
    }


    /**
     * 创建线上领取小样订单
     *
     * @param user         用户id
     * @param spuId        spuId
     * @param settlementId 支付类型
     */
    public Map<String, String> receiveBeautyOrder(User user, String spuId, String addressId, Integer settlementId, HttpServletRequest request) {
        GoodsSpu goodsSpu = selectOneById(spuId, GoodsSpu.class);
        ValueCheckUtils.notEmpty(goodsSpu, "未找到商品记录");

        ShoppingAddress shoppingAddress = selectOneById(addressId, ShoppingAddress.class);
        ValueCheckUtils.notEmpty(shoppingAddress, "未找到收货地址记录");

        return transactionTemplate.execute(new TransactionCallback<Map<String, String>>() {
            @Nullable
            @Override
            public Map<String, String> doInTransaction(TransactionStatus transactionStatus) {
                //小样邮费
                BigDecimal freight = goodsSpu.getFreight();
                if (user.getMemberType() == UserConst.USER_MEMBER_TYPE_COMMON) {
                    freight = BigDecimalUtils.multiply(freight, new BigDecimal(0.95));
                }

                switch (settlementId) {
                    case SettlementConst.SETTLEMENT_WECHAT_PAY://微信支付对接
                        //TODO 领取小样回调URL
                        String receiveBeautyNotifyUrl = globalConfigService.get(GlobalConfigService.Enum.APP_DOMAIN_NAME) + ShopConst.WeChatPayReceiveBeautyNotifyURL;

                        String orderNumber = OrderNoUtils.buildOrderNo();
                        Timestamp createTime = new Timestamp(System.currentTimeMillis());
                        OrderInfo orderInfo = new OrderInfo(
                                orderNumber, settlementId, user.getId(), ShopConst.SHOP_OFFICIAL, user.getUserName(), addressId, freight, BigDecimal.ZERO, ShopConst.ORDER_TYPE_BECOME_MEMBER,
                                ShopConst.ORDER_STATUS_NONE, createTime
                        );
                        orderInfo.setIfPay(false);
                        orderInfo.setIfInvestigation(true);
                        insertIfNotNull(orderInfo);

                        OrderItem orderItem = new OrderItem(orderInfo.getId(), spuId, "", 1, BigDecimal.ZERO, freight, freight);
                        insertIfNotNull(orderItem);

                        Map<String, String> map = weChatPayService.weChatPrePay(
                                orderInfo.getId(), freight, "美妆链年卡会员办理", WeChatPayCommonUtils.getRemoteHost(request), receiveBeautyNotifyUrl
                        );

                        //扣库存
                        goodsSpuMapper.decreaseSpuStock(orderItem.getSpuId(), 1);

                        map.put(OrderInfo.Order_no, orderNumber);
                        map.put(OrderInfo.Create_time, createTime.toString());
                        return map;

                    case SettlementConst.SETTLEMENT_ALI_PAY://支付宝对接
                        return null;

                    default:
                        throw new BusinessException("错误支付方式");
                }
            }
        });
    }


    /**
     * 扫码领取小样
     *
     * @param userId    用户id
     * @param qrCodeStr 小样编码
     */
    public Map<String, String> scanBeautyOrder(String userId, String qrCodeStr) {
        Map<String, String> urlParamMap = MineStringUtils.convertUrlParam2Map(qrCodeStr);

        //TODO 提取qrCodeStr
        String aisleCode = urlParamMap.get("a");
        String machineCode = urlParamMap.get("m");

        User user = selectOneById(userId, User.class);
        if (StringUtils.isBlank(user.getWxOpenId())) {
            throw new BusinessException("请绑定微信号后再扫码领取");
        }
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                Map<String, String> paramMap = ImmutableMap.of("openid", user.getWxOpenId(), "aisle_code", aisleCode, "machine_code", machineCode);
                String result = HttpClientUtils.doPost("http://pinkjewelry.cn/pinkjewelry/payment/freeCollenctionGoods", paramMap, HttpClientUtils.CHARSET);
                CodeEntity<Beauty> codeEntity = JSONObject.parseObject(result, CodeEntity.class);
                if (codeEntity.getCode() != "00") {
                    throw new BusinessException(ScanBeautyUtils.buildMessage(codeEntity.getCode()));
                }
                Beauty beauty = codeEntity.getData();

                GoodsSpu beautyGoodsSpu = GoodsSpu.buildBeautyGoodsSpu(beauty);
                insertIfNotNull(beautyGoodsSpu);

                Timestamp createTime = new Timestamp(System.currentTimeMillis());
                String orderNumber = OrderNoUtils.buildOrderNo();
                OrderInfo orderInfo = new OrderInfo(
                        orderNumber, beautyGoodsSpu.getSettlementId(), userId, ShopConst.SHOP_OFFICIAL, user.getUserName(), "", BigDecimal.ZERO,
                        beautyGoodsSpu.getFreight(), ShopConst.ORDER_TYPE_SCAN_BEAUTY, ShopConst.ORDER_STATUS_RECEIVE, createTime
                );
                orderInfo.setIfPay(true);
                orderInfo.setIfInvestigation(true);

                insertIfNotNull(orderInfo);

                OrderItem orderItem = new OrderItem(orderInfo.getId(), beautyGoodsSpu.getSpuId(), "", 1, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
                insertIfNotNull(orderItem);


                //完成任务，并增加颜值
                missionService.finishMission(user, MissionConst.OBTAIN_BEAUTY, "APP线上领取小样");

                if (user.getType() == UserConst.USER_MERCHANT_TYPE_BEING) {
                    //查询用户商户签约记录
                    Map<String, Object> whereMap = ImmutableMap.of(UserMerchantRecord.User_id + "=", userId, UserMerchantRecord.If_valid + "=", true);
                    UserMerchantRecord userMerchantRecord = selectOneByWhereMap(whereMap, UserMerchantRecord.class);
                    if (userMerchantRecord != null) {
                        UserMerchantRuler userMerchantRuler = userMerchantService.filterMerchantRulerById(userMerchantRecord.getMerchantRulerId());
                        if (userMerchantRuler != null) {
                            String inviteId = user.getInviteId();
                            UserAssets userAssets = userAssetsService.getAssetsByUserId(inviteId);

                            Mission mission = missionService.filterTaskByCode(MissionConst.OBTAIN_BEAUTY); //颜值任务奖励
                            //颜值分润值
                            MissionReward missionReward = missionService.getMissionRewardByMemberType(user.getMemberType(), mission);
                            BigDecimal computeShareProfit = BigDecimalUtils.multiply(
                                    missionReward.getValue(),
                                    BigDecimalUtils.percentToRate(userMerchantRuler.getComputeReturnRate())
                            );
                            //增加邀请用户颜值
                            Integer computeSettlementId = missionReward.getSettlementId();
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
                                    inviteId, AssetTurnoverConst.TURNOVER_TYPE_BEAUTY_RECEIVE, beautyServiceShareProfit, AssetTurnoverConst.COMPANY_ID,
                                    inviteId, userAssets, computeSettlementId, "被邀请用户线上领取美妆样品服务费"
                            );
                        }

                    } else {
                        logger.error("用户id为：" + userId + "的用户类型异常，该用户现用户类型为商户，但在商户签约表中无有效记录");
                    }
                }
            }
        });

        return null;
    }


    /**
     * 卡券兑换礼包
     *
     * @param userId 用户id
     * @param spuId  商品id
     */
    public Map<String, String> exchangeSpuPackageOrder(String userId, String voucherId, String spuId, String addressId) {
        String flag = redisClient.get(RedisKeyConst.USE_VOUCHER_FLAG);
        if ("0".equals(flag)) {
            throw new BusinessException("暂不支持兑换礼包,请稍后再试");
        }

        ShoppingAddress shoppingAddress = selectOneById(addressId, ShoppingAddress.class);
        ValueCheckUtils.notEmpty(shoppingAddress, "未找到收货地址记录");

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                UserVoucherRecord userVoucherRecord = userVoucherMapper.getUserVoucher(userId, voucherId);
                if (userVoucherRecord == null) {
                    throw new UserVoucherNoneException();
                }

                if (shopVoucherService.checkIfPackageVoucher(userVoucherRecord.getVoucherType())) {
                    //作废用户优惠券
                    if (userVoucherMapper.cancelVoucherValidity(userId, voucherId) == 0) {
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

                    Timestamp createTime = new Timestamp(System.currentTimeMillis());
                    String orderNumber = OrderNoUtils.buildOrderNo();
                    OrderInfo orderInfo = new OrderInfo(
                            orderNumber, settlementId, userId, shopId, user.getUserName(), addressId, new BigDecimal(0), orderTotalPrice, ShopConst.ORDER_TYPE_EXCHANGE_BEAUTY,
                            ShopConst.ORDER_STATUS_NONE, createTime
                    );
                    orderInfo.setIfPay(true);
                    orderInfo.setIfInvestigation(true);

                    insertIfNotNull(orderInfo);

                    OrderItem orderItem = new OrderItem(orderInfo.getId(), spuId, "", 1, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
                    insertIfNotNull(orderItem);

                    //扣库存
                    goodsSpuMapper.decreaseSpuStock(orderItem.getSpuId(), 1);

                    missionService.finishMission(user, MissionConst.EXCHANGE_PACKAGE, "兑换礼包");

                    if (user.getType() == UserConst.USER_MERCHANT_TYPE_BEING) {
                        //查询用户商户签约记录
                        Map<String, Object> whereMap = ImmutableMap.of(UserMerchantRecord.User_id + "=", userId, UserMerchantRecord.If_valid + "=", true);
                        UserMerchantRecord userMerchantRecord = selectOneByWhereMap(whereMap, UserMerchantRecord.class);
                        if (userMerchantRecord != null) {
                            Mission mission = missionService.filterTaskByCode(MissionConst.EXCHANGE_PACKAGE); //兑换礼包奖励值
                            UserMerchantRuler userMerchantRuler = userMerchantService.filterMerchantRulerById(userMerchantRecord.getMerchantRulerId());
                            if (userMerchantRuler != null) {
                                String inviteId = user.getInviteId();
                                UserAssets userAssets = userAssetsService.getAssetsByUserId(inviteId);

                                //颜值分润值
                                MissionReward missionReward = missionService.getMissionRewardByMemberType(user.getMemberType(), mission);
                                BigDecimal computeShareProfit = BigDecimalUtils.multiply(
                                        missionReward.getValue(),
                                        BigDecimalUtils.percentToRate(userMerchantRuler.getComputeReturnRate()
                                        ));

                                //增加邀请用户颜值
                                Integer computeSettlementId = missionReward.getSettlementId();
                                userAssetsService.changeUserAssets(inviteId, computeShareProfit, computeSettlementId, userAssets);
                                assetsTurnoverService.createAssetsTurnover(
                                        inviteId, AssetTurnoverConst.TURNOVER_TYPE_COMPUTE_RETURN, computeShareProfit, AssetTurnoverConst.COMPANY_ID,
                                        inviteId, userAssets, computeSettlementId, "被邀请用户线上兑换礼包颜值返现分利"
                                );
                            }

                        } else {
                            logger.error("用户id为：" + userId + "的用户类型异常，该用户现用户类型为商户，但在商户签约表中无有效记录");
                        }
                    }

                } else {
                    throw new BusinessException("暂只支持年卡会员卡券，生日卡券使用");
                }
            }
        });
        return null;
    }


    /**
     * 创建办理年卡订单
     *
     * @param userId 用户id
     */
    public Map<String, String> becomeMemberOrder(String userId, Integer settlementId, HttpServletRequest request) {
        String flag = redisClient.get(RedisKeyConst.BECOME_MEMBER_FLAG);
        if ("0".equals(flag)) {
            throw new BusinessException("暂不支持办理年卡会员,请稍后再试");
        }

        User user = selectOneById(userId, User.class);
        if (Objects.equals(user.getMemberType(), UserConst.USER_MEMBER_TYPE_COMMON)) {
            throw new BusinessException("您已是PINKER年卡会员，无须重复办理");
        }
        //TODO 办理年卡回调URL
        String becomeMemberNotifyUrl = globalConfigService.get(GlobalConfigService.Enum.APP_DOMAIN_NAME) + ShopConst.WeChatPayBecomeMemberNotifyURL;

        return transactionTemplate.execute(new TransactionCallback<Map<String, String>>() {
            @Nullable
            @Override
            public Map<String, String> doInTransaction(TransactionStatus transactionStatus) {
                switch (settlementId) {
                    case SettlementConst.SETTLEMENT_WECHAT_PAY://微信支付对接
                        //年卡会员费用
                        BigDecimal bigDecimal = new BigDecimal(globalConfigService.get(GlobalConfigService.Enum.MEMBER_FREE));
                        String orderNumber = OrderNoUtils.buildOrderNo();
                        Timestamp createTime = new Timestamp(System.currentTimeMillis());
                        OrderInfo orderInfo = new OrderInfo(
                                orderNumber, settlementId, userId, ShopConst.SHOP_OFFICIAL, user.getUserName(), "", bigDecimal, BigDecimal.ZERO, ShopConst.ORDER_TYPE_BECOME_MEMBER,
                                ShopConst.ORDER_STATUS_RECEIVE, createTime
                        );
                        orderInfo.setIfPay(false);

                        return weChatPayService.weChatPrePay(
                                orderInfo.getId(), bigDecimal, "美妆链年卡会员办理", WeChatPayCommonUtils.getRemoteHost(request), becomeMemberNotifyUrl
                        );

//            case SettlementConst.SETTLEMENT_ALI_PAY://支付宝对接
//                break;

                    default:
                        throw new BusinessException("错误支付方式");
                }
            }
        });


    }


    /**
     * APP用户的订单列表
     *
     * @param userId      用户id
     * @param orderStatus 订单状态
     * @param orderType   订单类型
     * @param pagination
     * @return
     */
    public List<OrderGoodsInfo> listAppOrderGoodsInfo(String userId, Integer orderStatus, Integer orderType, Pagination pagination) {
        List<OrderGoodsInfo> orderGoodsInfoList;
        orderGoodsInfoList = orderMapper.listUserOrderGoods(userId, orderStatus, orderType, pagination);
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
    public OrderGoodsInfo getAppOrderGoodsInfo(String userId, String orderId) {
        OrderGoodsInfo orderGoodsInfo = orderMapper.getUserOrderGoodsInfo(userId, orderId);
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

    /**
     * 更新订单状态
     *
     * @param orderId
     * @param status
     */
    public void updateOrderStatus(String orderId, Integer status) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setId(orderId);
        orderInfo.setStatus(status);
        if (updateIfNotNull(orderInfo) == 0) {
            throw new BusinessException("更新订单状态失败");
        }
    }

    /**
     * 更新可填写问卷的订单状态
     *
     * @param orderId
     * @param status
     */
    public void updateInvestOrderStatus(String orderId, Integer status) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setId(orderId);
        orderInfo.setStatus(status);
        orderInfo.setIfInvestigation(false);
        if (updateIfNotNull(orderInfo) == 0) {
            throw new BusinessException("更新订单状态失败");
        }
    }


    /**
     * 确认收货
     *
     * @param userId
     * @param orderId
     */
    public void receive(String userId, String orderId) {
        Map<String, Object> whereMap = ImmutableMap.of(OrderInfo.User_id + "=", userId, OrderInfo.Id + "=", orderId);
        OrderInfo orderInfo = selectOneByWhereMap(whereMap, OrderInfo.class);

        Integer orderType = orderInfo.getOrderType();
        ValueCheckUtils.notEmpty(orderType, "订单类型异常");
        switch (orderType) {
            case ShopConst.ORDER_TYPE_SHOPPING: //商城代币购物

                break;

            case ShopConst.ORDER_TYPE_RECEIVE_BEAUTY://线上领取小样
                updateInvestOrderStatus(orderId, ShopConst.ORDER_STATUS_RECEIVE);
                break;

            case ShopConst.ORDER_TYPE_SCAN_BEAUTY://线下扫码领取小样
                updateInvestOrderStatus(orderId, ShopConst.ORDER_STATUS_RECEIVE);
                break;

            case ShopConst.ORDER_TYPE_EXCHANGE_BEAUTY://线上兑换小样礼包
                updateInvestOrderStatus(orderId, ShopConst.ORDER_STATUS_RECEIVE);
                break;

            case ShopConst.ORDER_TYPE_BECOME_MEMBER://办理年卡会员

                break;

            default:
                throw new BusinessException("数据异常");
        }

    }
}
