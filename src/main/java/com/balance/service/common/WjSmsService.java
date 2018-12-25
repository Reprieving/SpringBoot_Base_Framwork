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

    private static String key;
    private static String Uid;

    @Autowired
    private GlobalConfigService globalConfigService;

    @PostConstruct
    private void init() {
        key = globalConfigService.get(GlobalConfigService.Enum.WJ_SMS_ACCESS_KEY);
        Uid = globalConfigService.get(GlobalConfigService.Enum.WJ_SMS_UID);
    }

    /**
     * 发送短信
     * @param phoneNumber 手机号
     * @param msgContent 短信内容
     */
    public void sendSms(String phoneNumber, String msgContent) {
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod("http://gbk.api.smschinese.cn");
        post.addRequestHeader("Article_content-Type", "application/x-www-form-urlencoded;charset=gbk");//在头文件中设置转码
        NameValuePair[] data = {new NameValuePair("Uid", Uid), new NameValuePair("Key", key), new NameValuePair("smsMob", phoneNumber), new NameValuePair("smsText", msgContent)};
        post.setRequestBody(data);
        String result = null;
        try {
            client.executeMethod(post);
            result = new String(post.getResponseBodyAsString().getBytes("gbk"));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        System.out.println(result); //打印返回消息状态

        post.releaseConnection();
    }

}
