package com.balance.utils;

import com.sun.org.apache.bcel.internal.generic.RETURN;

public class ScanBeautyUtils {
    public static String buildMessage(String code) {
        switch (code) {
            case "00":
                return "";

            case "ERROR03":
                return "请先注册后再扫码领取";

            case "ERROR15":
                return "机器或货道不存在";

            case "ERROR07":
                return "此货道缺货";

            case "ERROR12":
                return "此货道非免费商品";

            case "ERROR13":
                return "本月两次免费领取完毕，第三次请联系客服，获取更多优惠，谢谢";

            case "ERROR14":
                return "您已领取过此商品，请在首次领取36小时后二次领取，谢谢";

            default:
                return "领取失败";
        }
    }
}
