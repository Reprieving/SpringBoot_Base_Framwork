package com.balance.architecture.mybatis.provider;


import com.balance.architecture.mybatis.TableUtil;
import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.utils.UUIDUtils;
import com.balance.utils.ValueCheckUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MysqlProvider {
    public static final String ID_VALUE = "idVal";

    private static final String AS = " AS ";
    public static final String PARAM_LIST = "tList";
    public static final String OBJECT_LIST = "objectList";
    public static final String CLAZZ = "clazz";
    public static final String ENTITY = "entity";
    public static final String PAGINATION = "pagination";
    private static final String IN = " IN ";
    private static final String EQUAL = " = ";
    private static final String PREFIX = "#{";
    private static final String SUFFIX = "}";
    private static final String PARAM = "'";

    public String insert(Map<String, Object> map) throws Exception {
        Class<?> clazz = (Class<?>) map.get(CLAZZ);
        Object object = map.get(ENTITY);
        String tableName = TableUtil.getTableName(clazz);
        List<String> dbColumnList = new ArrayList<>(20);
        List<Object> voAttrList = new ArrayList<>(20);
        Field[] fields = object.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];

            Column column = f.getAnnotation(Column.class);
            Id id = f.getAnnotation(Id.class);
            if(id!=null){
                f.setAccessible(true);
                f.set(object, UUIDUtils.createUUID());
            }
            if (column != null) {
                dbColumnList.add(column.name());
                f.setAccessible(true);
                voAttrList.add(checkValue(f.get(object)));
            }
        }
        return new SQL() {{
            INSERT_INTO(tableName).VALUES(StringUtils.join(dbColumnList, ","), StringUtils.join(voAttrList, ","));
        }}.toString();
    }

    public String insertIfNotNull(Map<String, Object> map) throws Exception {
        Class<?> clazz = (Class<?>) map.get(CLAZZ);
        Object object = map.get(ENTITY);
        String tableName = TableUtil.getTableName(clazz);
        List<String> dbColumnList = new ArrayList<>(20);
        List<Object> voAttrList = new ArrayList<>(20);
        Field[] fields = object.getClass()
                .getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            Column column = f.getAnnotation(Column.class);
            Id id = f.getAnnotation(Id.class);
            if(id!=null) {
                f.setAccessible(true);
                f.set(object, UUIDUtils.createUUID());
            }
            if (column != null) {
                f.setAccessible(true);
                Object attrVal = fields[i].get(object);
                if(attrVal != null){
                    dbColumnList.add(column.name());
                    voAttrList.add(checkValue(f.get(object)));
                }
            }
        }
        return new SQL() {{
            INSERT_INTO(tableName).VALUES(StringUtils.join(dbColumnList, ","), StringUtils.join(voAttrList, ","));
        }}.toString();
    }

    public String delete(Map<String, Object> map) throws Exception {
        Class<?> clazz = (Class<?>) map.get(CLAZZ);
        Object object = map.get(ENTITY);
        String tableName = TableUtil.getTableName(clazz);
        Field[] fields = object.getClass()
                .getDeclaredFields();
        Object idPoVal = null;
        String idDbColumn = "";
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            Id id_annotation = f.getAnnotation(Id.class);
            if (id_annotation != null) {
                f.setAccessible(true);
                Column column_annotation = f.getAnnotation(Column.class);
                idDbColumn = column_annotation.name();
                idPoVal = f.get(object);
            }
        }
        ValueCheckUtils.notEmpty(tableName, clazz.getName() + " need Table annotation");
        ValueCheckUtils.notEmpty(idDbColumn, clazz.getName() + " need Id annotation");

        String finalIdDbColumn = idDbColumn;
        Object finalIdPoColumn = idPoVal;
        return new SQL() {{
            DELETE_FROM(tableName);
            WHERE(finalIdDbColumn + EQUAL + checkValue(finalIdPoColumn));
        }}.toString();
    }

    public String update(Map<String, Object> map) throws Exception {
        Class<?> clazz = (Class<?>) map.get(CLAZZ);
        Object object = map.get(ENTITY);
        String tableName = TableUtil.getTableName(clazz);
        List<String> setList = new ArrayList<>(20);
        String idDbColumn = "";
        Object idPoVal = null;
        Field[] fields = object.getClass()
                .getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            f.setAccessible(true);
            Column column = f.getAnnotation(Column.class);
            Id id_annotation = f.getAnnotation(Id.class);
            if (id_annotation != null) {
                idDbColumn = column.name();
                idPoVal = f.get(object);
            }
            if (column != null && id_annotation == null) {
                setList.add(column.name() + EQUAL + checkValue(f.get(object)));
            }
        }

        ValueCheckUtils.notEmpty(tableName, clazz.getName() + " need Table annotation");
        ValueCheckUtils.notEmpty(idDbColumn, clazz.getName() + " need Id annotation");


        String finalIdDbColumn = idDbColumn;
        Object finalIdPoVal = idPoVal;
        return new SQL() {{
            UPDATE(tableName);
            SET(StringUtils.join(setList.toArray(), ","));
            WHERE(finalIdDbColumn + EQUAL + checkValue(finalIdPoVal));
        }}.toString();
    }


    public String selectById(Map<String, Object> objects) throws Exception {
        Class<?> clazz = (Class<?>) objects.get(CLAZZ);
        Object object = clazz.newInstance();
        String tableName = TableUtil.getTableName(clazz);
        List<String> dbColumnList = new ArrayList<>(20);
        String idDbColumn = "";
        Field[] fields = object.getClass()
                .getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            boolean isId = false;
            Field f = fields[i];
            String voColumn = f.getName();
            Id id_annotation = f.getAnnotation(Id.class);
            if (id_annotation != null) {
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
        
        String finalIdDbColumn = idDbColumn;
        return new SQL() {{
            SELECT(StringUtils.join(dbColumnList, ","));
            FROM(tableName);
            WHERE(finalIdDbColumn + EQUAL + PREFIX + ID_VALUE + SUFFIX);
        }}.toString();
    }

    public String selectAll(Map<String, Object> objects) throws Exception {
        Class<?> clazz = (Class<?>) objects.get(CLAZZ);
        String tableName = TableUtil.getTableName(clazz);
        List<String> dbColumnList = new ArrayList<>(20);
        String idDbColumn = "";
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            boolean isId = false;
            Field f = fields[i];
            String voColumn = f.getName();
            Id id_annotation = f.getAnnotation(Id.class);
            if (id_annotation != null) {
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

        return new SQL() {{
            SELECT(StringUtils.join(dbColumnList, ","));
            FROM(tableName);
        }}.toString();
    }

    public Object checkValue(Object object){
        if(!(object instanceof Boolean)){
            object = PARAM+object+PARAM;
        }
        return object;
    }

    public static void main(String[] args) {
        System.out.println(0 % 100);
    }
}
