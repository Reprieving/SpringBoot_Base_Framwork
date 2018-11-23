package com.balance.utils;

import org.apache.commons.lang.time.DateFormatUtils;

import java.util.Random;

public class RandomUtil {

    //验证码字符列表
    private static final char STUFFS[] = {
            'A','B','C','D','E','F','G','H',
            'I','J','K','L','M','N','P','Q',
            'R','S','T','U','V','W','X','Y',
            '1','2','3','4','5','6','7','8'};

    private static final char[] RANDOM_CHARS = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};

    /**
     * 获取指定字符数的随机数字字符串
     *
     * @param num 字符个数
     * @return String
     */
    public static String getRandomNumStr(int num) {
        StringBuilder str = new StringBuilder();
        Random rdm = new Random();
        for (int i = 0; i < num; i++) {
            int index = rdm.nextInt(10);
            str.append(RANDOM_CHARS[index]);
        }
        return str.toString();
    }

    // nextInt取值:[0,x)
    public static int randomIndex(int size) {
        Random random = new Random();
        return random.nextInt(size);
    }

    public static String randomNumber(Integer length) {

        String str = "";

        for (int i = 0; i < length; i++) {
            int index = randomIndex(9);
            str = str + String.valueOf(index);
        }

        return str;
    }

    /** 自定义进制(0,1没有加入,容易与o,l混淆) */
    private static final char[] r=new char[]{'q', 'w', 'e', '8', 'a', 's', '2', 'd', 'z', 'x', '9', 'c', '7', 'p', '5', 'i', 'k', '3', 'm', 'j', 'u', 'f', 'r', '4', 'v', 'y', 'l', 't', 'n', '6', 'b', 'g', 'h'};

    /** (不能与自定义进制有重复) */
    private static final char b='o';

    /** 进制长度 */
    private static final int binLen=r.length;

    /** 序列最小长度 */
    private static final int s=6;

    /**
     * 根据ID生成六位随机码
     * @param id ID
     * @return 随机码
     */
    public static String randomInviteCode(long id) {
        char[] buf=new char[32];
        int charPos=32;

        while((id / binLen) > 0) {
            int ind=(int)(id % binLen);
            // System.out.println(num + "-->" + ind);
            buf[--charPos]=r[ind];
            id /= binLen;
        }
        buf[--charPos]=r[(int)(id % binLen)];
        // System.out.println(num + "-->" + num % binLen);
        String str=new String(buf, charPos, (32 - charPos));
        // 不够长度的自动随机补全
        if(str.length() < s) {
            StringBuilder sb=new StringBuilder();
            sb.append(b);
            Random rnd=new Random();
            for(int i=1; i < s - str.length(); i++) {
                sb.append(r[rnd.nextInt(binLen)]);
            }
            str+=sb.toString();
        }
        return str;
    }

    /**
     * 根据随机码翻译成id
     * @param code ID
     * @return 随机码
     */
    public static long codeToId(String code) {
        char chs[]=code.toCharArray();
        long res=0L;
        for(int i=0; i < chs.length; i++) {
            int ind=0;
            for(int j=0; j < binLen; j++) {
                if(chs[i] == r[j]) {
                    ind=j;
                    break;
                }
            }
            if(chs[i] == b) {
                break;
            }
            if(i > 0) {
                res=res * binLen + ind;
            } else {
                res=ind;
            }
        }
        return res;
    }

}
