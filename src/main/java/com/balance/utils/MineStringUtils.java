package com.balance.utils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MineStringUtils {

    /**
     * 数值类型前面补零（共13位）
     * @param num
     * @return
     */
    public static String supplementZeroGenerateThirteen(int num){
        String str = String.format("%013d", num);

        return str;
    }

    /**
     * 数值类型前面补零（共16位）
     * @param num
     * @return
     */
    public static String supplementZeroGenerateSixteen(int num){
        String str = String.format("%016d", num);

        return str;
    }
    /**
     * 数值类型前面补零（共3位）
     * @param num
     * @return
     */
    public static String supplementZeroGenerateThree(int num){
        String str = String.format("%03d", num);

        return str;
    }

    /**
     * 判断字符串是不是double型
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]+[.]{0,1}[0-9]*[dD]{0,1}");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    public static String trim(String str, boolean nullFlag){
        String tempStr = null;

        if (str != null)
        {
            tempStr = str.trim();
        }

        if (nullFlag)
        {
            if ("".equals(tempStr) || "null".equals(tempStr))
            {
                tempStr = null;
            }
        }
        else
        {
            if (tempStr == null)
            {
                tempStr = "";
            }
        }

        return tempStr;
    }
    public static String replace(String strSource, String strFrom, String strTo) {
        if(strSource==null){
            return null;
        }
        int i = 0;
        if ((i = strSource.indexOf(strFrom, i)) >= 0) {
            char[] cSrc = strSource.toCharArray();
            char[] cTo = strTo.toCharArray();
            int len = strFrom.length();
            StringBuffer buf = new StringBuffer(cSrc.length);
            buf.append(cSrc, 0, i).append(cTo);
            i += len;
            int j = i;
            while ((i = strSource.indexOf(strFrom, i)) > 0) {
                buf.append(cSrc, j, i - j).append(cTo);
                i += len;
                j = i;
            }
            buf.append(cSrc, j, cSrc.length - j);
            return buf.toString();
        }
        return strSource;
    }


    public static String deal(String str) {
        str = replace(str, "\\", "\\\\");
        str = replace(str, "'", "\\'");
        str = replace(str, "\r", "\\r");
        str = replace(str, "\n", "\\n");
        str = replace(str, "\"", "\\\"");
        return str;
    }

    public static String GetMapToXML(Map<String,String> param){
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        for (Map.Entry<String,String> entry : param.entrySet()) {
            sb.append("<"+ entry.getKey() +">");
            sb.append(entry.getValue());
            sb.append("</"+ entry.getKey() +">");
        }
        sb.append("</xml>");
        return sb.toString();
    }

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
        String s = "[#{tList[1].id}, #{tList[1].parentsId}, #{tList[1].projectsId}, #{tList[1].versionNo}, #{tList[1].interfaceName}, #{tList[1].httpType}, #{tList[1].url}, #{tList[1].reqParamContent}, #{tList[1].rspParamContent}, #{tList[1].isProjectRoot}, #{tList[1].isDirectory}, #{tList[1].ifValid}, #{tList[1].createBy}, #{tList[1].createTime}]:[#{tList[1].id}, #{tList[1].parentsId}, #{tList[1].projectsId}, #{tList[1].versionNo}, #{tList[1].interfaceName}, #{tList[1].httpType}, #{tList[1].url}, #{tList[1].reqParamContent}, #{tList[1].rspParamContent}, #{tList[1].isProjectRoot}, #{tList[1].isDirectory}, #{tList[1].ifValid}, #{tList[1].createBy}, #{tList[1].createTime}]";
        String[] strArr = s.split(":");
        System.out.println(strArr);
    }
}
