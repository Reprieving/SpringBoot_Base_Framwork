package com.balance.architecture.mybatis;

import com.balance.architecture.mybatis.annotation.Table;

public class TableUtil {
    public static String getTableName(Class<?> clazz) throws Exception {
        Table table = clazz.getAnnotation(Table.class);
        if (table == null) {
            throw new Exception(clazz.getName() + " need table annotation");
        }
        return table.name();
    }


}
