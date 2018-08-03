package com.balance.core.mybatis.provider;


import com.balance.core.mybatis.TableUtil;
import com.balance.core.mybatis.annotation.Column;
import com.balance.core.mybatis.annotation.Id;
import com.balance.utils.ValueCheckUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MysqlProvider {
    private static final String AS = " AS ";
    public static final String PARAM_LIST = "tList";
    public static final String CLAZZ = "clazz";
    public static final String PAGINATION = "pagination";
    private static final String IN = " IN ";
    private static final String EQUAL = " = ";
    private static final String PREFIX = "#{";
    private static final String SUFFIX = "}";
    private static final String PARAM = "'";

    public String insert(Object object) throws Exception {
        Class<?> clazz = object.getClass();
        String tableName = TableUtil.getTableName(clazz);
        List<String> dbColumnList = new ArrayList<>(20);
        List<Object> voAttrList = new ArrayList<>(20);
        Field[] fields = object.getClass()
                .getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            Column column = f.getAnnotation(Column.class);
            if (column != null) {
                f.setAccessible(true);
                dbColumnList.add(column.name());
                voAttrList.add(PREFIX + f.getName() + SUFFIX);
            }
        }
        return new SQL() {{
            INSERT_INTO(tableName).VALUES(StringUtils.join(dbColumnList, ","), StringUtils.join(voAttrList, ","));
        }}.toString();
    }

    public String insertSelective(Object object) throws Exception {
        Class<?> clazz = object.getClass();
        String tableName = TableUtil.getTableName(clazz);
        List<String> dbColumnList = new ArrayList<>(20);
        List<Object> voAttrList = new ArrayList<>(20);
        Field[] fields = object.getClass()
                .getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Column column = fields[i].getAnnotation(Column.class);
            Object attrVal = fields[i].get(clazz);
            if (column != null && attrVal != null) {
                dbColumnList.add(column.name());
                voAttrList.add(attrVal);
            }
        }
        return new SQL() {{
            INSERT_INTO(tableName).VALUES(StringUtils.join(dbColumnList, ","), StringUtils.join(voAttrList, ","));
        }}.toString();
    }

    public String delete(Object object) throws Exception {
        Class<?> clazz = object.getClass();
        String tableName = TableUtil.getTableName(clazz);
        Field[] fields = object.getClass()
                .getDeclaredFields();
        String idPoColumn = "";
        String idDbColumn = "";
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            Id id_annotation = f.getAnnotation(Id.class);
            if (id_annotation != null) {
                f.setAccessible(true);
                Column column_annotation = f.getAnnotation(Column.class);
                idDbColumn = column_annotation.name();
                idPoColumn = f.getName();
            }
        }

        ValueCheckUtils.notEmpty(tableName, clazz.getName() + " need Table annotation");
        ValueCheckUtils.notEmpty(idDbColumn, clazz.getName() + " need Id annotation");

        String finalIdDbColumn = idDbColumn;
        String finalIdPoColumn = idPoColumn;
        return new SQL() {{
            DELETE_FROM(tableName);
            WHERE(finalIdDbColumn + PREFIX + finalIdPoColumn + SUFFIX);
        }}.toString();
    }

    public String update(Object object) throws Exception {
        Class<?> clazz = object.getClass();
        String tableName = TableUtil.getTableName(clazz);
        List<String> setList = new ArrayList<>(20);
        String idPoColumn = "";
        String idDbColumn = "";

        Field[] fields = object.getClass()
                .getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            Column column = f.getAnnotation(Column.class);
            Id id_annotation = f.getAnnotation(Id.class);
            if (id_annotation != null) {
                idPoColumn = f.getName();
                idDbColumn = column.name();
            }
            if (column != null && id_annotation == null) {
                f.setAccessible(true);
                setList.add(column.name() + EQUAL + PREFIX + f.get(clazz) + SUFFIX);
            }
        }

        ValueCheckUtils.notEmpty(tableName, clazz.getName() + " need Table annotation");
        ValueCheckUtils.notEmpty(idDbColumn, clazz.getName() + " need Id annotation");


        String finalIdDbColumn = idDbColumn;
        String finalIdPoColumn = idPoColumn;
        return new SQL() {{
            UPDATE(tableName);
            SET(StringUtils.join(setList.toArray(), ","));
            WHERE(finalIdDbColumn + EQUAL + PREFIX + finalIdPoColumn + SUFFIX);
        }}.toString();
    }


    public String selectById(Object object) throws Exception {
        Class<?> clazz = object.getClass();
        String tableName = TableUtil.getTableName(clazz);
        List<String> dbColumnList = new ArrayList<>(20);
        String idPoColumn = "";
        String idDbColumn = "";
        Field[] fields = object.getClass()
                .getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            boolean isId = false;
            Field f = fields[i];
            String voColumn = f.getName();
            Id id_annotation = f.getAnnotation(Id.class);
            if (id_annotation != null) {
                idPoColumn = voColumn;
                f.setAccessible(true);
                isId = true;
            }
            Column column_annotation = f.getAnnotation(Column.class);
            if (column_annotation != null) {
                String dbColumn = column_annotation.name();
                if (isId) {
                    idDbColumn = dbColumn;
                }
                dbColumnList.add(dbColumn + " " + AS + " " + voColumn);
            }
        }

        ValueCheckUtils.notEmpty(tableName, clazz.getName() + " need Table annotation");
        ValueCheckUtils.notEmpty(idDbColumn, clazz.getName() + " need Id annotation");

        String finalIdColumn = idPoColumn;
        String finalIdDbColumn = idDbColumn;
        return new SQL() {{
            SELECT(StringUtils.join(dbColumnList, ","));
            FROM(tableName);
            WHERE(finalIdDbColumn + EQUAL + PREFIX + finalIdColumn + SUFFIX);
        }}.toString();
    }

    public String selectListByIds(Map<String, Object> objects) throws Exception {
        List<Object> objectList = (List<Object>) objects.get(PARAM_LIST);
        ValueCheckUtils.notEmpty(objectList, objectList.getClass()
                .getName() + " data list's size must greater than zero");
        Class<?> clazz = (Class<?>) objects.get(CLAZZ);
        String tableName = TableUtil.getTableName(clazz);
        List<String> dbColumnList = new ArrayList<>(20);
        List<Object> idList = new ArrayList<>(20);
        String idDbColumn = "";
        Field[] fields = clazz.getDeclaredFields();
        for (int j = 0; j < objectList.size(); j++) {
            for (int i = 0; i < fields.length; i++) {
                boolean isId = false;
                Field f = fields[i];
                String voColumn = f.getName();
                Id id_annotation = f.getAnnotation(Id.class);
                if (id_annotation != null) {
                    isId = true;
                    idList.add(PARAM + objectList.get(j) + PARAM);
                }
                Column column_annotation = f.getAnnotation(Column.class);
                if (column_annotation != null) {
                    String dbColumn = column_annotation.name();
                    if (isId) {
                        idDbColumn = dbColumn;
                    }
                    if(j<1){
                        dbColumnList.add(dbColumn + " " + AS + " " + voColumn);
                    }
                }
            }
        }
        ValueCheckUtils.notEmpty(tableName, clazz.getName() + " need Table annotation");
        ValueCheckUtils.notEmpty(idDbColumn, clazz.getName() + " need Id annotation");

        String finalIdDbColumn = idDbColumn;
        return new SQL() {{
            SELECT(StringUtils.join(dbColumnList, ","));
            FROM(tableName);
            WHERE(finalIdDbColumn + IN + "(" + StringUtils.join(idList, ",") + ")");
        }}.toString();
    }
}
