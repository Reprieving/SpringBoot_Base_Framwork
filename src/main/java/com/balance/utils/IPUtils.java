package com.balance.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
public class IPUtils {

    /**
     * 获取客户端IP<br>
     * 默认检测的Header：<br>
     * 1、X-Forwarded-For<br>
     * 2、X-Real-IP<br>
     * 3、Proxy-Client-IP<br>
     * 4、WL-Proxy-Client-IP<br>
     * otherHeaderNames参数用于自定义检测的Header
     *
     * @param request 请求对象
     * @return IP地址
     */
    public static String getClientIP(HttpServletRequest request) {
        String[] headers = { "X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR" };
        String ip;
        for (String header : headers) {
            ip = request.getHeader(header);
            if (!isUnknow(ip)) {
                return getMultistageReverseProxyIp(ip);
            }
        }

        ip = request.getRemoteAddr();
        return getMultistageReverseProxyIp(ip);
    }

    /**
     * 从多级反向代理中获得第一个非unknown IP地址
     *
     * @param ip 获得的IP地址
     * @return 第一个非unknown IP地址
     */
    public static String getMultistageReverseProxyIp(String ip) {
        // 多级反向代理检测
        if (ip != null && ip.indexOf(",") > 0) {
            final String[] ips = ip.trim().split(",");
            for (String subIp : ips) {
                if (!isUnknow(subIp)) {
                    ip = subIp;
                    break;
                }
            }
        }
        return ip;
    }

    /**
     * 检测给定字符串是否为未知，多用于检测HTTP请求相关<br>
     *
     * @param checkString 被检测的字符串
     * @return 是否未知
     */
    public static boolean isUnknow(String checkString) {
        return StringUtils.isBlank(checkString) || "unknown".equalsIgnoreCase(checkString);
    }
}
