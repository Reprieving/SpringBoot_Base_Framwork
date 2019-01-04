package com.balance.architecture.utils;

import com.balance.architecture.dto.Result;
import com.balance.architecture.exception.BusinessException;
import com.balance.constance.ShopConst;
import com.balance.utils.MineStringUtils;

import java.util.HashMap;
import java.util.Map;

public class ResultUtils {
    public static final Integer RSP_SUCCESS = 0;//normal request
    public static final Integer RSP_FAIL = -1;//error request
    public static final Integer RSP_LOGIN = 1;//re-login

    public static final Integer RSP_USER_VOUCHER_RECORD_NONE = -1000;//用户没有兑换券


    public static final String WECHATPAY_RETURN_CODE_FIELD = "return_code";
    public static final String WECHATPAY_RETURN_MSG_FIELD = "return_msg";
    public static final String WECHATPAY_RETURN_SUCCESS_CODE = "SUCCESS";
    public static final String WECHATPAY_RETURN_FAIL_CODE = "FAIL";

    //request success
    public static Result success() {
        Result result = new Result();
        result.setStateCode(RSP_SUCCESS);
        return result;
    }

    //request success
    public static Result success(Object data, String msg) {
        Result result = new Result();
        result.setStateCode(RSP_SUCCESS);
        result.setMessage(msg);
        result.setData(data);
        return result;
    }

    //request success
    public static Result success(Object data, Integer count) {
        Result result = new Result();
        result.setStateCode(RSP_SUCCESS);
        result.setCount(count);
        result.setData(data);
        return result;
    }

    //request success
    public static Result success(Object data) {
        Result result = new Result();
        result.setStateCode(RSP_SUCCESS);
        result.setData(data);
        return result;
    }

    //request success
    public static Result success(String msg) {
        Result result = new Result();
        result.setStateCode(RSP_SUCCESS);
        result.setMessage(msg);
        return result;
    }

    //request fail
    public static Result error(int code, String msg) {
        Result result = new Result();
        result.setStateCode(RSP_FAIL);
        result.setMessage(msg);
        return result;
    }

    //request fail
    public static Result error(String msg) {
        Result result = new Result();
        result.setStateCode(RSP_FAIL);
        result.setMessage(msg);
        return result;
    }

    public static Result<?> error(Integer rspUserVoucherRecordNone, Map<String, Object> map, String message) {
        Result result = new Result();
        result.setStateCode(rspUserVoucherRecordNone);
        result.setData(map);
        result.setMessage(message);
        return result;

    }

    //request fail
    public static Result reLogin(String msg) {
        Result result = new Result();
        result.setStateCode(RSP_LOGIN);
        result.setMessage(msg);
        return result;
    }

    //weChatPay success
    public static Object weChatPaySuccess(Map<String, String> returnMap) {
        returnMap.put(WECHATPAY_RETURN_CODE_FIELD, WECHATPAY_RETURN_SUCCESS_CODE);
        returnMap.put(WECHATPAY_RETURN_MSG_FIELD, "OK");
        return MineStringUtils.GetMapToXML(returnMap);
    }

    //weChatPay success
    public static String weChatPaySuccess() {
        Map<String, String> returnMap = new HashMap<>();
        returnMap.put(WECHATPAY_RETURN_CODE_FIELD, WECHATPAY_RETURN_SUCCESS_CODE);
        returnMap.put(WECHATPAY_RETURN_MSG_FIELD, "OK");
        return MineStringUtils.GetMapToXML(returnMap);
    }

    //weChatPay fail
    public static String weChatPayFail() {
        Map<String, String> returnMap = new HashMap<>();
        // 支付失败
        returnMap.put(WECHATPAY_RETURN_CODE_FIELD, WECHATPAY_RETURN_FAIL_CODE);
        returnMap.put(WECHATPAY_RETURN_MSG_FIELD, "error");
        return MineStringUtils.GetMapToXML(returnMap);
    }


    /**
     * 订单请求返回参数处理
     *
     * @param orderType    订单类型
     * @param settlementId 支付类型
     * @param returnMap    返回的内容
     * @return
     */
    public static Object orderPay(Integer orderType, Integer settlementId, Map<String, String> returnMap) {
        switch (orderType) {
//            case ShopConst.ORDER_TYPE_SHOPPING: //商城代币购物
//                break;

            case ShopConst.ORDER_TYPE_RECEIVE_BEAUTY://线上领取小样
//                receiveBeautyOrder(user,shopOrderPayReq.getSpuId(),shopOrderPayReq.getAddressId(),shopOrderPayReq.getSettlementId(),request);
                return weChatPaySuccess(returnMap);

//            case ShopConst.ORDER_TYPE_SCAN_BEAUTY://线下扫码领取小样
//                break;
//
//            case ShopConst.ORDER_TYPE_EXCHANGE_BEAUTY://线上兑换小样礼包
//                break;

            case ShopConst.ORDER_TYPE_BECOME_MEMBER://办理年卡会员
                return weChatPaySuccess(returnMap);

            default:
                return success(returnMap, "订单创建成功");
        }


    }


}
