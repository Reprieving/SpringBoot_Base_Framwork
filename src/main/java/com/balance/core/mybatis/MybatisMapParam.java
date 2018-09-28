package com.balance.core.mybatis;

import java.util.HashMap;

public class MybatisMapParam extends HashMap {
    // key名称
    public static final String KEY_FIELD = "keyField";
    // value名称
    public static final String VALUE_FIELD = "valueField";
    // value值类型
    public static final String VALUE_CLASS = "valueClass";
    public MybatisMapParam(){    }
    public MybatisMapParam(String keyField, String valueField, String valueClass){
        this.put(KEY_FIELD, keyField);
        this.put(VALUE_FIELD, valueField);
        this.put(VALUE_CLASS, valueClass);    }
    // value值类型枚举类
    public enum ValueClass {
        INTEGER("integer"),
        BIG_DECIMAL("bigDecimal");
        private String code;
        public String getCode() {
            return code;
        }
        ValueClass(String code){
            this.code = code;
        }
    }
}
