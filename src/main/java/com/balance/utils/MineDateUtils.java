package com.balance.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class MineDateUtils {
    /**
     * 指定日期加上天数后的日期
     * @param currentDate 创建时间
     * @param num 为增加的天数
     * @return
     * @throws ParseException
     */
    public static Timestamp plusDay(int num, Date currentDate){
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DATE, num);
        currentDate = ca.getTime();
        return new Timestamp(currentDate.getTime());
    }


}
