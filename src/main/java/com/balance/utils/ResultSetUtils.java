package com.balance.utils;

import com.balance.core.mybatis.annotation.Column;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultSetUtils {
    private ResultSetUtils() {
    }

    public static <T> List<T> getBeans(ResultSet resultSet, Class<T> className) {
        List<T> list = new ArrayList<T>();
        Field fields[] = className.getDeclaredFields();
        try {
            resultSet.first();
            do {
                T instance = className.newInstance();
                for (Field field : fields) {
                    if(field.getAnnotation(Column.class)==null){
                        continue;
                    }
                    Object result = resultSet.getObject(field.getName());
                    boolean flag = field.isAccessible();
                    field.setAccessible(true);
                    field.set(instance, result);
                    field.setAccessible(flag);
                }
                list.add(instance);
            }while (resultSet.next());

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static <T> T getBean(ResultSet resultSet, Class<T> className) {
        T instance = null;
        try {
            instance = className.newInstance();
            Field fields[] = className.getDeclaredFields();
            for (Field field : fields) {
                if(field.getAnnotation(Column.class)==null){
                    continue;
                }
                Object result = resultSet.getObject(field.getName());
                boolean flag = field.isAccessible();
                field.setAccessible(true);
                field.set(instance, result);
                field.setAccessible(flag);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return instance;
    }

    public static <T> List<T>  result2List(ResultSet resultSet, Class<T> className) throws SQLException, IllegalAccessException, InstantiationException {
        List<T> list = new ArrayList<T>();
        Field fields[] = className.getDeclaredFields();
        while (resultSet.next()){
            T instance = className.newInstance();
            for (Field field : fields) {
                if(field.getAnnotation(Column.class)==null){
                    continue;
                }
                Object result = resultSet.getObject(field.getName());
                boolean flag = field.isAccessible();
                field.setAccessible(true);
                field.set(instance, result);
                field.setAccessible(flag);
            }
            list.add(instance);
        }
        return list;
    }
}
