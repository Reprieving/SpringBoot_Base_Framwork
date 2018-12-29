package com.balance.utils;

public class OrderNoUtils {

    // 生成订单号
    public static String buildOrderNo() {
        return String.valueOf((int) (Math.random() * (10)))
                + String.valueOf((int) (Math.random() * (10)))
                + String.valueOf(System.currentTimeMillis());
    }

}
