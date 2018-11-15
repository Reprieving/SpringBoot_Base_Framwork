package com.balance.architecture.utils;

import java.util.Map;

public class EntityUtils {
    private static Map<String,Object> getAppointFieldMap(Class t, Map<String,Object> entityMap, Map<String,Object> conditionMap){
//        try {
//            for(Map.Entry<String,Object> m : conditionMap.entrySet()){
//                Field fields = t.getDeclaredField(m.getKey());
//                String columnName = fields.getAnnotation(Column.class).name();
//
//                System.out.println(columnName + " " + m.getValue() + " " + entityMap.get(m.getKey()));
//            }
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }
        return null;
    }


    public static void main(String[] args) {
        String userName = "";
        userName = "1' OR '1=1";
        String sql = "SELECT * FROM users WHERE name = '" + userName + "';";
        System.out.println(sql);


    }
}
