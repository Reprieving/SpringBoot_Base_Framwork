package com.balance.core.mybatis;

import com.balance.core.mybatis.annotation.Table;

import java.lang.reflect.Field;

public class TableUtil {
    public static String getTableName(Class<?> clazz) throws Exception {
        Table table = clazz.getAnnotation(Table.class);
        if (table == null) {
            throw new Exception(clazz.getName() + " need table annotation");
        }
        return table.name();
    }


}
