package com.balance.architecture.mybatis;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.exception.EntityLackTableAnnotationException;
import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MybatisBuildUtil {
    public static String getTableName(Class<?> clazz) throws EntityLackTableAnnotationException {
        Table table = clazz.getAnnotation(Table.class);
        if (table == null) {
            throw new EntityLackTableAnnotationException(clazz.getName() + " need table annotation");
        }
        return table.name();
    }

    public static MybatisMapperParam buildMapperParam4Select(Serializable id, Map<String, Object> whereMap, Pagination pagination,Class clazz, Map<String, Object> orderMap) throws IllegalAccessException, InstantiationException, EntityLackTableAnnotationException {
        Object object = clazz.newInstance();
        String tableName = getTableName(clazz);
        List<String> dbColumnList = new ArrayList<>(20);
        String idDbColumn = "";
        Field[] fields = object.getClass().getDeclaredFields();
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
                dbColumnList.add(dbColumn + " AS " + voColumn);
            }
        }

        MybatisMapperParam mybatisMapperParam = new MybatisMapperParam();
        mybatisMapperParam.setDbColumnList(dbColumnList);
        mybatisMapperParam.setTableName(tableName);
        mybatisMapperParam.setWhereMap(whereMap);
        mybatisMapperParam.setClazz(clazz);
        mybatisMapperParam.setIdDbColumn(idDbColumn);
        mybatisMapperParam.setIdPoVal(id);
        mybatisMapperParam.setPagination(pagination);
        mybatisMapperParam.setOrderMap(orderMap);
        return mybatisMapperParam;
    }

    public static MybatisMapperParam buildMapperParam4SelectOne(Map<String, Object> whereMap, Class clazz) throws IllegalAccessException, InstantiationException, EntityLackTableAnnotationException {
        return buildMapperParam4Select(null,whereMap,null,clazz,null);
    }

    public static MybatisMapperParam buildMapperParam4SelectListByWhere(Map<String, Object> whereMap,Pagination pagination, Class clazz,Map<String,Object> orderMap) throws IllegalAccessException, InstantiationException, EntityLackTableAnnotationException {
        return buildMapperParam4Select(null,whereMap,pagination,clazz,orderMap);
    }

    public static MybatisMapperParam buildMapperParam4SelectById(Serializable id, Class clazz) throws IllegalAccessException, InstantiationException, EntityLackTableAnnotationException {
        return buildMapperParam4Select(id,null,null,clazz,null);
    }

    public static MybatisMapperParam buildMapperParam4SelectAll(Pagination pagination, Class clazz, Map<String,Object> orderMap) throws IllegalAccessException, InstantiationException, EntityLackTableAnnotationException {
        return buildMapperParam4SelectListByWhere(null,pagination,clazz,orderMap);
    }



}
