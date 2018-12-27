package com.balance.utils;

import com.balance.architecture.exception.BusinessException;

import java.util.List;

public class ValueCheckUtils {
//    public static void notEmpty(CharSequence o,String message) {
//        if (null == o || "".equals(o.toString()
//                .trim()) || o.length() == 0) {
//            throw new IllegalArgumentException(message);
//        }
//    }

    public static void notEmpty(Object o,String message) {
        if (null == o ) {
            throw new BusinessException(message);
        }
    }

    public static void notEmpty(List<?> o, String message) {
        if (null == o || o.size()==0) {
            throw new BusinessException(message);
        }
    }

    public static void notZero(Integer i, String message) {
        if(i==0){
            throw new BusinessException(message);
        }
    }
}