package com.balance.utils;

import java.math.BigDecimal;

/**
 * Created by bint on 2018/8/25.
 */
public class BigDecimallUtils {

    /**
     * 相乘
     * @param bigDecimal1
     * @param bigDecimal2
     * @return
     */
    public static BigDecimal multiply(BigDecimal bigDecimal1, BigDecimal bigDecimal2) {
        return bigDecimal1.multiply(bigDecimal2);
    }

    /**
     * 相加
     * @param bigDecimal1
     * @param bigDecimal2
     * @return
     */
    public static BigDecimal add(BigDecimal bigDecimal1, BigDecimal bigDecimal2) {
        return bigDecimal1.add(bigDecimal2);
    }


    /**
     * 相除
     * @param bigDecimal1
     * @param bigDecimal2
     * @return
     */
    public static BigDecimal divide(BigDecimal bigDecimal1, BigDecimal bigDecimal2) {
        return bigDecimal1.divide(bigDecimal2, SCALE_EIGHT);
    }

    /**
     * 相除
     *
     * @param bigDecimal1
     * @param bigDecimal2
     * @return
     */
    public static BigDecimal subtract(BigDecimal bigDecimal1, BigDecimal bigDecimal2) {
        return bigDecimal1.subtract(bigDecimal2);
    }

    public static BigDecimal setScale(BigDecimal bigDecimal) {
        return bigDecimal.setScale(SCALE_EIGHT, ROUND_FLOOR);
    }


    public static BigDecimal newObject(Double value) {

        BigDecimal bigDecimal = new BigDecimal(value);

        bigDecimal = bigDecimal.setScale(SCALE_EIGHT, ROUND_FLOOR);
        return bigDecimal;
    }


    public static BigDecimal newObject(Integer value) {

        Double valueDouble = Double.valueOf(value);

        return newObject(valueDouble);
    }

    /**
     * 保留6位小数
     */
    static Integer SCALE_EIGHT = 6;
    /**
     * 保留方式 - 截取 ， 参见 BigDecimal.ROUND_FLOOR
     */
    static Integer ROUND_FLOOR = 3;
}
