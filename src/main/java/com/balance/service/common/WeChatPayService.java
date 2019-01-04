package com.balance.service.common;

import com.balance.architecture.service.BaseService;
import com.balance.entity.common.WeChatPayNotifyRecord;
import com.balance.exception.WeChatPayNotifyException;
import com.balance.utils.BigDecimalUtils;
import com.balance.utils.WeChatPayCommonUtils;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Service
public class WeChatPayService extends BaseService {

    @Autowired
    private GlobalConfigService globalConfigService;

    /**
     * 微信预支付
     *
     * @param tradeNo
     * @param orderPrice
     * @param description
     * @param requestIp
     * @param notifyUrl
     * @return
     */
    public Map<String, String> weChatPrePay(String tradeNo, BigDecimal orderPrice, String description, String requestIp, String notifyUrl) {
        String weChatPayAppId = globalConfigService.get(GlobalConfigService.Enum.WECHAT_PAY_APP_ID);
        String mchId = globalConfigService.get(GlobalConfigService.Enum.WECHAT_MCH_ID);

        SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
        parameterMap.put("appid", weChatPayAppId);  //应用appid
        parameterMap.put("mch_id", mchId);  //商户号
        parameterMap.put("nonce_str", WeChatPayCommonUtils.getRandomString(32));
        parameterMap.put("body", description);
        parameterMap.put("out_trade_no", tradeNo);
        parameterMap.put("fee_type", "CNY");
        DecimalFormat df = new java.text.DecimalFormat("0");
        parameterMap.put("total_fee", df.format(BigDecimalUtils.multiply(orderPrice, new BigDecimal(100))));
        parameterMap.put("spbill_create_ip", requestIp);
        parameterMap.put("notify_url", notifyUrl);
        parameterMap.put("trade_type", "APP");
        String sign = WeChatPayCommonUtils.createSign("UTF-8", parameterMap);
        parameterMap.put("sign", sign);
        String requestXML = WeChatPayCommonUtils.getRequestXml(parameterMap);

        String result = WeChatPayCommonUtils.httpsRequest("https://api.mch.weixin.qq.com/pay/unifiedorder", "POST", requestXML);
        Map<String, String> resultMap = null;
        try {
            resultMap = WeChatPayCommonUtils.doXMLParse(result);
            resultMap.put("package","Sign=WXPay");
            resultMap.put("timestamp", String.valueOf(Instant.now().getEpochSecond()));
        } catch (JDOMException | IOException e) {
            throw new WeChatPayNotifyException("认证异常");
        }

        return resultMap;
    }


    /**
     * 微信支付通知数据解析
     *
     * @param inStream
     * @return
     */
    public WeChatPayNotifyRecord notifyAnalyse(InputStream inStream) throws WeChatPayNotifyException {
        try {
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            String resultXml = new String(outSteam.toByteArray(), "utf-8");
            Map<String, String> params = WeChatPayCommonUtils.doXMLParse(resultXml);
            outSteam.close();
            inStream.close();

            if (!WeChatPayCommonUtils.isWechatPaySign(params)) {
                throw new WeChatPayNotifyException("认证异常");

//            // 支付失败
//            return_data.put("return_code", "FAIL");
//            return_data.put("return_msg", "return_code不正确");
//            return MineStringUtils.GetMapToXML(return_data);
            } else {
                String mchId = params.get("mch_id");
                String mchOrderId = params.get("transaction_id");
                String wxOrderId = params.get("out_trade_no");
                String wxAppId = params.get("appid");
                String openId = params.get("openid");
                BigDecimal orderPrice = BigDecimalUtils.multiply(new BigDecimal(params.get("total_fee")), new BigDecimal(100));
                Timestamp createTime = new Timestamp(System.currentTimeMillis());

                WeChatPayNotifyRecord weChatPayNotifyRecord = new WeChatPayNotifyRecord(mchId, mchOrderId, wxOrderId, wxAppId, openId, orderPrice, createTime);

                return weChatPayNotifyRecord;

//            return_data.put("return_code", "SUCCESS");
//            return_data.put("return_msg", "OK");
//            return MineStringUtils.GetMapToXML(return_data);

            }
        } catch (IOException | JDOMException e) {
            throw new WeChatPayNotifyException("认证异常");
        }
    }


    public static void main(String[] args) {
        System.out.println(Instant.now().getEpochSecond());
    }

}
