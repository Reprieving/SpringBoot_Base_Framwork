package com.balance.utils;

import java.util.HashMap;

public class MineStringUtils {
    public static String firstCharToUpperCase(String str) {
        char[] cs = str.toCharArray();
        if (cs[0] >= 97 && cs[0] <= 122) {
            cs[0] -= 32;
        }
        return String.valueOf(cs);
    }


    public static String replaceFirstEndChar(String str,String firstChar,String endChar,String spliceChar) {
        String[] strArr = str.split(spliceChar);
        String resultStr = "";
        for(int i = 0; i<strArr.length;i++){
            Integer strLength = strArr[i].length();
            StringBuilder sb = new StringBuilder(strArr[i]);
            sb.replace(0,1,firstChar);
            sb.replace(strLength-1,strLength,endChar);
            resultStr += sb.toString()+",";
        }
        return resultStr.substring(resultStr.length()-1,resultStr.length());
    }

    public static void main(String[] args) {
        HashMap map = new HashMap();
        map.put("1","1");
        String s = "[#{tList[1].id}, #{tList[1].parentsId}, #{tList[1].projectsId}, #{tList[1].versionNo}, #{tList[1].interfaceName}, #{tList[1].httpType}, #{tList[1].url}, #{tList[1].reqParamContent}, #{tList[1].rspParamContent}, #{tList[1].isProjectRoot}, #{tList[1].isDirectory}, #{tList[1].isValid}, #{tList[1].createBy}, #{tList[1].createTime}]:[#{tList[1].id}, #{tList[1].parentsId}, #{tList[1].projectsId}, #{tList[1].versionNo}, #{tList[1].interfaceName}, #{tList[1].httpType}, #{tList[1].url}, #{tList[1].reqParamContent}, #{tList[1].rspParamContent}, #{tList[1].isProjectRoot}, #{tList[1].isDirectory}, #{tList[1].isValid}, #{tList[1].createBy}, #{tList[1].createTime}]";
        String[] strArr = s.split(":");
        System.out.println(strArr);
    }
}
