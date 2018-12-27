package com.balance.service.common;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * Created by bint on 2018/8/20.
 */
@Service
public class WjSmsService {

    private static final Logger logger = LoggerFactory.getLogger(WjSmsService.class);

    private static String token;
    private static String sign;

    @Autowired
    private GlobalConfigService globalConfigService;

    @PostConstruct
    private void init() {
        token = globalConfigService.get(GlobalConfigService.Enum.WJ_SMS_TOKEN);
        sign = globalConfigService.get(GlobalConfigService.Enum.WJ_SMS_SIGN);
    }

    /**
     * 发送短信
     *
     * @param phoneNumber  手机号
     * @param tpl_id       模板id
     * @param params       模板内容
     * @param country_code 国家代码
     */
    public void sendSms(String phoneNumber, String tpl_id, String params, String country_code) {
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod("http://www.api51.cn/api/smsApi/sendcode");
        post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");//在头文件中设置转码
        NameValuePair[] data = {
                new NameValuePair("token", token), new NameValuePair("sign", sign),
                new NameValuePair("mobile", phoneNumber), new NameValuePair("tpl_id", tpl_id),
                new NameValuePair("params", params), new NameValuePair("country_code", country_code),
        };
        post.setRequestBody(data);
        String result = null;
        try {
            client.executeMethod(post);
            result = new String(post.getResponseBodyAsString().getBytes("utf-8"));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        System.out.println(result); //打印返回消息状态

        post.releaseConnection();
    }

}
